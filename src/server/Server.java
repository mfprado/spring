package server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;

public class Server {

    private static final int CR = 0x0d;
    private static final int LF = 0x0a;
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start()  throws IOException {
        var server = new ServerSocket(this.port);
        while (true) {
            try {
                System.out.println("Waiting client request");
                var socket = server.accept();
                System.out.println("New client connected");
                var input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                try {
                    var firstLine = this.readLine(input);
                    System.out.println(firstLine);
                } catch (IOException e) {
                    System.out.println("Error reading");
                }

            } catch (IOException e) {
                System.out.println("Connection error " + e.getMessage());
            }
        }
    }

    private String readLine(InputStream input) throws IOException{
        StringBuilder sb = new StringBuilder();
        int c = input.read();
        while (c >= 0 && c != LF) {
            if (c != CR) {
                sb.append((char) c);
            }
            c = input.read();
        }
        return sb.toString();
    }
}
