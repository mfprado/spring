package com.mfprado.server.handler.reader;


import com.mfprado.server.handler.exception.InvalidRequestException;
import com.mfprado.server.http.HttpHeaders;
import com.mfprado.server.http.Protocol;
import com.mfprado.server.http.URL;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;

public class URLParser {

    public URL parse(String firstLine, Map<String, List<String>> headers) throws InvalidRequestException {
        var tokenizer = new StringTokenizer(firstLine);
        tokenizer.nextToken();
        var url = tokenizer.nextToken();

        if (url.length() > 0) {
            int start = 0;
            int i, c;
            String query = null, path = "", protocol = "http";
            int limit = url.length();
            for (i = start; (i < limit) && ((c = url.charAt(i)) != '/'); i++) {
                if (c == ':') {
                    protocol = url.substring(start, i).toLowerCase();
                    start = i + 1;
                }
            }

            if (start < limit) {
                int queryStart = url.indexOf('?');
                if ((queryStart != -1) && (queryStart < limit)) {
                    query = url.substring(queryStart + 1, limit);
                    limit = queryStart;
                    url = url.substring(0, queryStart);
                }
            }

            var host = Optional.ofNullable(headers.get(HttpHeaders.HOST))
                    .flatMap(l -> l.stream().findFirst())
                    .orElse("localhost:9090");

            //path
            if (start < limit) {
                if (url.charAt(start) == '/') {
                    path = url.substring(start, limit);
                } else {
                    String seperator = "/";
                    path = seperator + url.substring(start, limit);
                }
            }


            return new URL(Protocol.valueOf(protocol.toUpperCase()), host, path, Optional.ofNullable(query));
        } else {
            throw new InvalidRequestException("Error parsing url");
        }


    }
}
