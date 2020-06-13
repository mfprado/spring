package com.mfprado.server.handler.reader;

import com.mfprado.server.handler.exception.InvalidHeadersException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeadersParser {
    public Map<String, List<String>> parse(ServletInputStream servletInputStream) throws InvalidHeadersException {
        try {
            Map<String, List<String>> headers = new HashMap<>();
            var line = servletInputStream.readLine();
            while (!line.equals("")) {
                var kv = line.split(": ");
                var values = List.of(kv[1].split(","));
                headers.put(kv[0], values);
                line = servletInputStream.readLine();
            }
            return headers;
        } catch (IOException e) {
            throw new InvalidHeadersException();
        }
    }
}
