package com.school.server;

import java.io.File;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
public class JavaTcpMemory {
    public static String testjdbc = "jdbc:iotdb://223.99.13.54:5098/";
    public static String jdbc = "jdbc:iotdb://172.16.50.6:6667/";
    public static String username = "root";
    public static String password = "root";
    public static String driver = "org.apache.iotdb.jdbc.IoTDBDriver";
    public static int port = 6066;

    public static String kafkaIp = "172.16.50.2";
    public static String kafkaPort = "9093";
    public static String kafkaTopic = "decodeInputAll";

    public static void updateMemoryFromXML() throws Exception{
        String engine_path = System.getProperty("user.dir");
        SAXReader saxReader = new SAXReader();

        Document document = saxReader.read(new File(engine_path + "/DataAccess.xml"));
        Element rootElement = document.getRootElement();
        testjdbc = rootElement.element("testjdbc").getTextTrim();
        jdbc = rootElement.element("jdbc").getTextTrim();
        username = rootElement.element("username").getTextTrim();
        password = rootElement.element("password").getTextTrim();
        driver = rootElement.element("driver").getTextTrim();
        port = Integer.valueOf(rootElement.element("port").getTextTrim());

        kafkaIp = rootElement.element("kafkaIp").getTextTrim();
        kafkaPort = rootElement.element("kafkaPort").getTextTrim();
        kafkaTopic = rootElement.element("kafkaTopic").getTextTrim();
    }

}
