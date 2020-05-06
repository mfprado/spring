package com.spring.server;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class HttpServletRequest {
    private static final String CONTENT_LENGTH = "Content-Length";
    private ServletInputStream servletInputStream;
    private HttpRequest request;

    public HttpServletRequest(InputStream input) {
        this.servletInputStream = new ServletInputStream(input);
        createRequest();
    }

    private void createRequest() {
        try {
            var firstLine = this.servletInputStream.readLine();
            if (firstLine.equals("")) return;

            var tokenizer = new StringTokenizer(firstLine);
            var method = HttpMethod.valueOf(tokenizer.nextToken().toUpperCase());
            var url = this.buildURL(tokenizer.nextToken());

            var headers = buildHeaders();

            if (method == HttpMethod.GET || method == HttpMethod.HEAD || !headers.containsKey(CONTENT_LENGTH)) {
                this.request = new HttpRequest(method, url, headers, Optional.empty());
            } else {
                var contentLength = Integer.parseInt(headers.get(CONTENT_LENGTH).get(0));
                var body = getBody(contentLength);

                this.request = new HttpRequest(method, url, headers, body);
            }

        } catch (IOException e) {
            System.out.println("Error creating request");
        }
    }

    public Optional<HttpRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    public  Optional<String> getBody(int contentLength) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            if (contentLength > 0) {
                int bytesRead = -1;
                while ((bytesRead = servletInputStream.read()) > 0) {
                    stringBuilder.append((char) bytesRead);
                    if (stringBuilder.length() == contentLength) break;
                }
            }
        } catch (IOException ex) {
            System.out.println("Error reading request body" + ex.getMessage());
            throw ex;
        }
        System.out.println(stringBuilder.toString());
        return Optional.of(stringBuilder.toString());
    }

    private Map<String, List<String>> buildHeaders() throws IOException {
        Map<String, List<String>> headers = new HashMap<>();
        var line = this.servletInputStream.readLine();
        while (!line.equals("")) {
            var kv = line.split(": ");
            var values = List.of(kv[1].split(","));
            headers.put(kv[0], values);
            line = this.servletInputStream.readLine();
        }
        return headers;
    }

    private URL buildURL(String url) {

        int start = 0;
        int i, c;
        String host = "", query = null, path = "", protocol = "http";
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

        i = 0;
        boolean isUNCName = (start <= limit - 4) &&
                (url.charAt(start) == '/') &&
                (url.charAt(start + 1) == '/') &&
                (url.charAt(start + 2) == '/') &&
                (url.charAt(start + 3) == '/');
        if (!isUNCName && (start <= limit - 2) && (url.charAt(start) == '/') &&
                (url.charAt(start + 1) == '/')) {
            start += 2;
            i = url.indexOf('/', start);
            if (i < 0 || i > limit) {
                i = url.indexOf('?', start);
                if (i < 0 || i > limit)
                    i = limit;
            }


            host = url.substring(start, i);
            int port;
            int ind = host.indexOf(':');
            port = -1;
            if (ind >= 0) {
                // port can be null according to RFC2396
                if (host.length() > (ind + 1)) {
                    port = Integer.parseInt(host.substring(ind + 1));
                }
                host = host.substring(0, ind);
            }
            if (port < -1)
                throw new IllegalArgumentException("Invalid port number :" +
                        port);
            start = i;

            path = "";

        }

        //path
        if (start < limit) {
            if (url.charAt(start) == '/') {
                path = url.substring(start, limit);
            } else {
                String seperator = "/";
                path = seperator + url.substring(start, limit);
            }
        }

        Map<String, String> queryParams = Optional.ofNullable(query).map(q -> List.of(q.split("&"))
                .stream().map(kv -> kv.split("="))
                .collect(Collectors.toMap(v -> v[0], v -> v[1]))
        ).orElse(Map.of());


        return new URL(Protocol.valueOf(protocol.toUpperCase()), host, path, queryParams);
    }

    public void closeInput() throws IOException {
        this.servletInputStream.closeInput();
    }
}
