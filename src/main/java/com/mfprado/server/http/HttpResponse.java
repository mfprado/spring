package com.mfprado.server.http;

import com.mfprado.jdk.ServletResponse;

import java.util.*;

public class HttpResponse implements ServletResponse {
    private HttpStatus status;
    private Map<String, List<String>> headers;
    private Optional<String> body;

    public HttpResponse(HttpStatus status, Map<String, List<String>> headers, Optional<String> body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public HttpResponse() {
        this.status = HttpStatus.OK;
        this.headers = new HashMap<>();
        this.body = Optional.empty();
    }

    @Override
    public int getStatusCode() {
        return status.getCode();
    }

    @Override
    public Optional<String> getBody() {
        return body;
    }

    @Override
    public Optional<List<String>> getHeaderValues(String name) {
        return Optional.ofNullable(headers.get(name));
    }

    @Override
    public Optional<String> getHeader(String name) {
        return getHeaderValues(name).flatMap(l -> l.stream().findFirst());
    }

    @Override
    public void setStatusCode(int statusCode) {
        status.setCode(statusCode);
    }

    @Override
    public void addHeaderValues(String name, List<String> values) {
        headers.put(name, values);
    }

    @Override
    public void addHeader(String name, String values) {
        Optional.ofNullable(headers.get(name))
                .orElseGet(() -> {
                    var valuesList = new ArrayList<String>();
                    headers.put(name, valuesList);
                    return valuesList;
                }).add(values);
    }

    @Override
    public void setBody(Optional<String> body) {
        this.body = body;
    }

    @Override
    public void setStatus(HttpStatus httpStatus) {
        this.status = httpStatus;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }
}

