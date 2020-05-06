package com.spring.server;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class SocketHandler implements Runnable {

    private final Socket socket;

    public SocketHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            var input = socket.getInputStream();
            var output = socket.getOutputStream();

            var httpServletRequest = new HttpServletRequest(input);

            var request = httpServletRequest.getRequest();
            if (request.isEmpty()) {
                httpServletRequest.closeInput();
                output.close();
                socket.close();
                return;
            }

            var httpServletResponse = new HttpServletResponse(new HttpResponse(HttpStatus.OK,
                    Map.of("Content-Type", List.of("text/html"),
                            "X-DESP-SANDBOX", List.of("true")),
                    Optional.of("response body")), output);

            httpServletResponse.send();

            httpServletRequest.closeInput();
            httpServletResponse.closeOutput();
            socket.close(); // no persist

        } catch (IOException e) {
            System.out.println("Error handling socket: " + e);
        }
    }
}
