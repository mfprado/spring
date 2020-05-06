package com.spring;

import com.spring.concurrency.ServerThreadPoolExecutor;
import com.spring.server.Server;

public class App {

    public static void main(String[] args) {
        Server server = new Server(9090, ServerThreadPoolExecutor.getExecutor());

        server.start();

        System.exit(-1);
    }
}
