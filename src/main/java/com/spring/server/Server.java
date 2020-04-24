package com.spring.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        var server = new ServerSocket(this.port);
        while (true) {
            try {
                System.out.println("Waiting client request");
                var socket = server.accept();
                System.out.println("New client connected");
                var handler = new SocketHandler(socket);
                handler.handle();
            } catch (IOException e) {
                System.out.println("Connection error " + e.getMessage());
            }
        }
    }
}
