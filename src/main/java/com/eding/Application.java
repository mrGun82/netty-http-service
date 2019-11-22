package com.eding;

import com.eding.http.server.ServerExample;

/**
 * @program:eding-cloud
 * @description:
 * @author:jiagang
 * @create 2019-11-22 10:37
 */
public class Application {
    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "8443" : "8080"));
    public static void main(String[] args) throws Exception {
        ServerExample server = new ServerExample();
        server.start(PORT);
    }
}
