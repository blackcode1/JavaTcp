package com.school.server;

import com.alibaba.fastjson.JSONObject;
import com.school.server.database.oprateDB;
import com.school.server.entity.requestPackageEntity;
import com.school.server.entity.responsePackageEntity;
import com.school.server.tool.byteTool;
import lombok.extern.log4j.Log4j;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.shaded.netty4.io.netty.handler.codec.mqtt.MqttConnAckMessage;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
@Log4j
public class SendThread extends Thread{
    public volatile boolean exit;
    private Socket server;
    private requestPackageEntity rEntity;
    private oprateDB database;

    public static class Fun implements MapFunction<String,String>{
        private requestPackageEntity rEntity;
        public Fun(requestPackageEntity rEntity){
            this.rEntity = rEntity;
        }

        @Override
        public String map(String s) throws Exception {
            JSONObject json = (JSONObject) JSONObject.parse(s);
            String data = byteTool.binaryString2hexString(json.getString("rawData"));
            responsePackageEntity packageEntity=new responsePackageEntity(rEntity,data);
            packageEntity.setPackageLength(byteTool.intToByteArray(data.length()/2));
            ArrayList <responsePackageEntity> packageList=new ArrayList<>();
            packageList.add(packageEntity);
//            this.sendData(server, packageList);
            return data;
        }
    }


    public void setExit(boolean exit){
        this.exit=exit;
    }

    SendThread(Socket server, requestPackageEntity rEntity,oprateDB database)
    {
        this.server = server;
        this.rEntity = rEntity;
        this.database = database;
        this.exit = false;
    }

    @Override
    public void run(){
        try {
            if (rEntity.getDataType() == 1) {
                ArrayList<responsePackageEntity> packageList = this.getResponsePackageEntities(rEntity, byteTool.bytesToDate(rEntity.getStartDate()), byteTool.bytesToDate(rEntity.getEndDate()));
                log.info("开始返回历史数据");
                this.sendData(server, packageList);
            } else {
                log.info("开始返回实时数据");
//                SimpleKafkaConsumer consumer = new SimpleKafkaConsumer(rEntity);
//                consumer.consume(server);
                KafkaConsumer<String, String> consumer;
                String TOPIC = JavaTcpMemory.kafkaTopic;
                Properties props = new Properties();
                props.put("bootstrap.servers", JavaTcpMemory.kafkaIp + ":" + JavaTcpMemory.kafkaPort);
                //每个消费者分配独立的组号
                String groupID = "group"+rEntity.hashCode() + new Random(1000);
                System.out.println(groupID);
                props.put("group.id", groupID);
                //如果value合法，则自动提交偏移量
                props.put("enable.auto.commit", "true");
                //设置多久一次更新被消费消息的偏移量
                props.put("auto.commit.interval.ms", "1000");
                //设置会话响应的时间，超过这个时间kafka可以选择放弃消费或者消费下一条消息
                props.put("session.timeout.ms", "11000");
                //自动重置offset
                props.put("auto.offset.reset","latest");
                props.put("key.deserializer",
                        "org.apache.kafka.common.serialization.StringDeserializer");
                props.put("value.deserializer",
                        "org.apache.kafka.common.serialization.StringDeserializer");
                consumer = new KafkaConsumer<String, String>(props);
                consumer.subscribe(Arrays.asList(TOPIC));
                while (true) {
                    if(exit)
                        break;
                    ConsumerRecords<String, String> records = consumer.poll(100);
                    for (ConsumerRecord<String, String> record : records){
                        System.out.println(System.currentTimeMillis());
                        JSONObject json = (JSONObject) JSONObject.parse(record.value());

                        String data = byteTool.binaryString2hexString(json.getString("rawData"));
//                        System.out.println(data.length());
//                        System.out.println("Originaldata:" + data);
//                        log.info(data);
                        data=data.substring(46);
                        responsePackageEntity packageEntity=new responsePackageEntity(rEntity,data);
                        packageEntity.setPackageLength(byteTool.intToByteArray(data.length()/2));
                       // System.out.println("length:" + byteTool.bytesToHex(packageEntity.getPackageLength()));
                        ArrayList<responsePackageEntity> packageList=new ArrayList<>();
                        packageList.add(packageEntity);
                     //   System.out.println("senddata:"+ packageEntity.toString());
                        sendData(server, packageList);
                    }

                }
            }
        }catch (Exception e){
            log.error(e);
        }
    }
    private ArrayList<responsePackageEntity> getResponsePackageEntities(requestPackageEntity rEntity,String startTime,String endTime) throws SQLException {
        String sql=oprateDB.makeSQL(rEntity.getLine(),rEntity.getCar(),rEntity.getCmd(),startTime,endTime);
        log.info("sql:"+sql);
        ResultSet resultSet=this.database.doSQL(sql);
        ArrayList <responsePackageEntity> packageList=new ArrayList<>();
        while(resultSet.next()){
            String result=resultSet.getString(2);
            result=result.substring(46);
            responsePackageEntity packageEntity=new responsePackageEntity(rEntity,result);
            packageEntity.setPackageLength(byteTool.intToByteArray(result.length()/2));
            log.info(":"+sql);
            packageList.add(packageEntity);
        }
        return packageList;
    }
    public void sendData(Socket server, ArrayList<responsePackageEntity> packageList) throws IOException {
        byte[] allCount= byteTool.intToByteArray(packageList.size());
        DataOutputStream out = new DataOutputStream(server.getOutputStream());
        for(int i=0;i<packageList.size();i++){
            if(this.exit){return;}
            responsePackageEntity mid= packageList.get(i);
            mid.setPackageAllCount(allCount);
            mid.setPackageNowCount(byteTool.intToByteArray(i+1));
            out.write(mid.toByteArray());
        }
    }

}
