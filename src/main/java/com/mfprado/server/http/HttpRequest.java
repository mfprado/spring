package com.mfprado.server.http;

import com.mfprado.jdk.ServletRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpRequest implements ServletRequest {
    private HttpMethod method;
    private URL url;
    private Map<String, List<String>> headers;
    private Optional<String> body;
    private String remoteAddress;

    public HttpRequest(HttpMethod method, URL url, Map<String, List<String>> headers, Optional<String> body) {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public String getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public void setRemoteAddress(String address) {
        this.remoteAddress = address;
    }

    @Override
    public String getHost() {
        return url.getHost();
    }

    @Override
    public String getMethod() {
        return method.name();
    }

    @Override
    public Optional<String> getHeader(String name) {
        return getHeaderValues(name).flatMap(v -> v.stream().findFirst());
    }

    @Override
    public Optional<List<String>> getHeaderValues(String name) {
        return Optional.ofNullable(this.headers.get(name));
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    @Override
    public String getPath() {
        return url.getPath();
    }

    @Override
    public Optional<String> getBody() {
        return body;
    }

    @Override
    public Optional<String> getQueryString() {
        return url.getQueryString();
    }

    public Map<String, String> getQueryParams() {
        return url.getQueryString().map(q -> List.of(q.split("&"))
                .stream().map(kv -> kv.split("="))
                .collect(Collectors.toMap(v -> v[0], v -> v[1]))
        ).orElse(Map.of());
    }

    public HttpRequest addHeader(String name, List<String> value) {
        this.headers.put(name, value);
        return this;
    }
}

