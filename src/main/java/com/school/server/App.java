package com.school.server;


import java.io.IOException;

/**
 * @author test
 */
public class App {
    public static void main(String [] args) throws Exception {
//        int port = 6066;
        JavaTcpMemory.updateMemoryFromXML();
        try
        {
            Thread t = new GreetingServer(JavaTcpMemory.port,false);
            t.start();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
