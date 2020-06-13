package com.mfprado.server.handler.writer;

import com.mfprado.server.http.HttpResponse;
import com.mfprado.server.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResponseParser {
    private static final String CR_LF = "\r\n";

    public String parse(HttpResponse httpResponse) {
        var builder = new StringBuilder();
        builder.append(parseStatus(httpResponse.getStatus()));
        builder.append(parseHeaders(httpResponse.getHeaders()));
        httpResponse.getBody().ifPresent(b -> builder.append(parseBody(b)));
        return builder.toString();
    }

    private String parseStatus(HttpStatus status) {
        return String.format(
                "HTTP/1.1 %s %s%s", status.getCode(), status.getDescription(), CR_LF);
    }

    private String parseHeaders(Map<String, List<String>> headers) {
        return headers.entrySet().stream()
                .map(this::createHeader)
                .collect(Collectors.joining(CR_LF));
    }

    private String createHeader(Map.Entry<String, List<String>> header) {
        var values = String.join(",", header.getValue());
        return String.format("%s: %s", header.getKey(), values);
    }

    private String parseBody(String body) {
        return CR_LF + CR_LF + body;
    }
}
