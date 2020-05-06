package com.spring.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Collectors;

public class HttpServletResponse {

    private static final String HTTP1_1 = "HTTP/1.1 ";
    private static final String CR_LF = "\r\n";

    private final HttpResponse httpResponse;
    private final OutputStream output;

    public HttpServletResponse(HttpResponse httpResponse, OutputStream output) {
        this.httpResponse = httpResponse;
        this.output = output;
    }

    public void send() throws IOException {
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

    public void closeOutput() throws IOException {
        output.close();
    }
}
