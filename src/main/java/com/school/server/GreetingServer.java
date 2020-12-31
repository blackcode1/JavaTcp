package com.school.server;

import java.net.*;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.school.server.database.oprateDB;
import com.school.server.entity.responsePackageEntity;
import com.school.server.tool.*;
import lombok.extern.log4j.Log4j;
import com.school.server.entity.requestPackageEntity;

@Log4j
public class GreetingServer extends Thread
{
   private ServerSocket serverSocket;
   private oprateDB database;
   private SendThread t;

   GreetingServer(int port,boolean debug) throws IOException
   {
      this.serverSocket = new ServerSocket(port);
      this.database = new oprateDB(debug);
   }
 
   @Override
   public void run()
   {
      while(true)
      {
         try
         {
            log.info("等待远程连接，端口号为：" + serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            log.info("已连接，远程主机地址：" + server.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(server.getInputStream());

            requestPackageEntity rEntity = getRequestPackageEntity(in);
            log.info("已接收到结束信号,接收数据为："+rEntity.toString());

            if(rEntity.getFunc()==1){
               this.t = new SendThread(server,rEntity,this.database);
               t.start();
               log.info("接收到发送信号,开启发送线程");
               requestPackageEntity nextR = getRequestPackageEntity(in);
               if(nextR.getFunc()==0){
                  t.setExit(true);
               }
            }
            server.close();
         }catch(SocketTimeoutException s)
         {
            log.error("Socket timed out!");
         } catch (IOException e){
            log.error(e.toString());
         }catch (Exception e){
            log.info("other error"+e.toString());
         }
      }
   }

   private requestPackageEntity getRequestPackageEntity(DataInputStream in) throws IOException {
      requestPackageEntity rEntity=new requestPackageEntity();
      in.readFully(rEntity.getStart());
      rEntity.setFunc(in.readByte());
      rEntity.setDataType(in.readByte());
      in.readFully(rEntity.getLine());
      in.readFully(rEntity.getCar());
      in.readFully(rEntity.getCmd());
      in.readFully(rEntity.getStartDate());
      in.readFully(rEntity.getEndDate());
      in.readFully(rEntity.getAck());
      in.readFully(rEntity.getReserve());
      in.readFully(rEntity.getEnd());
      return rEntity;
   }
}