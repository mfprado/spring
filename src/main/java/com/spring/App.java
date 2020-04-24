package com.spring;
import java.io.IOException;
import com.spring.server.Server;

public class App {

    public static void main(String[] args) {
        Server server = new Server(9090);

        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.exit(-1);
    }
}
