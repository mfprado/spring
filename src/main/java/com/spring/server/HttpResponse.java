package com.spring.server;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpResponse {
    private HttpStatus status;
    private Map<String, List<String>> headers;
    private Optional<String> body;

    public HttpResponse(HttpStatus status, Map<String, List<String>> headers, Optional<String> body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public Optional<String> getBody() {
        return body;
    }

    public void setBody(Optional<String> body) {
        this.body = body;
    }
}

