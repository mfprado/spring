package com.spring.server;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class SocketHandler {

    private static final String HTTP1_1 = "HTTP/1.1 ";
    private static final String CR_LF = "\r\n";

    private final Socket socket;
    private final RequestDeserializer requestDeserializer;

    public SocketHandler(Socket socket) {
        this.socket = socket;
        this.requestDeserializer = new RequestDeserializer();
    }

    public void handle() {
        try {
            var input = new BufferedInputStream(socket.getInputStream());
            var output = socket.getOutputStream();

            var request = requestDeserializer.deserialize(input);
            if (request.isEmpty()) {
                input.close();
                output.close();
                socket.close();
                return;
            }

            //Dispatcher Servlet
            var response = new HttpResponse(HttpStatus.OK,
                    Map.of("Content-Type:", List.of("text/html"),
                            "X-DESP-SANDBOX", List.of("true")),
                    Optional.of("response body"));

            sendResponse(output, response);

            input.close();
            output.flush();
            output.close();
            socket.close(); // no persist

        } catch (IOException e) {
            //TODO handle
            System.out.println("Error handling socket: " + e);
        }
    }

    private void sendResponse(OutputStream output, HttpResponse httpResponse) throws IOException {

        var code = httpResponse.getStatus().getCode();
        var statusDescription = httpResponse.getStatus().getDescription();
        var headers = httpResponse.getHeaders();
        var body = httpResponse.getBody();

        String response =
                HTTP1_1 + code + " " + statusDescription + CR_LF +
                headers.entrySet().stream()
                        .map(header -> header.getKey() + ": " + String.join(", ", header.getValue()) + CR_LF)
                        .collect(Collectors.joining()) +
                CR_LF + CR_LF + body.orElse("") + CR_LF + CR_LF;

        byte[] s = response.getBytes();
        output.write(s);
    }
}
