package com.spring.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    private static final byte[] SERVICE_UNAVAILABLE = ("HTTP/1.1 503 Service Unavailable \r\nRetry-After: 100\r\n\r\nService Unavailable").getBytes();
    private ServerSocket serverSocket;
    private int port;
    private ThreadPoolExecutor threadPoolExecutor;

    public Server(int port, ThreadPoolExecutor threadPoolExecutor) {
        this.port = port;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void start() {
        openServerSocket();
        while (true) {
            try {
                System.out.println("Waiting client request");
                var socket = serverSocket.accept();
                System.out.println("New client connected");
                try {
                    threadPoolExecutor.execute(new SocketHandler(socket));
                } catch (RejectedExecutionException e) {
                    rejectConnection(socket);
                }
            } catch (IOException e) {
                System.out.println("Connection error " + e.getMessage());
            }
        }
    }

    private void openServerSocket() {
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }

    private void rejectConnection(Socket socket) throws IOException {
        socket.getOutputStream().write(SERVICE_UNAVAILABLE);
        socket.getInputStream().close();
        socket.getOutputStream().close();
        socket.close();
    }
}
