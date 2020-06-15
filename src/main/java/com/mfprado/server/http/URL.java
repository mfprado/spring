package com.mfprado.server.http;

import java.util.Optional;

public class URL {
    private Protocol protocol;
    private String host;
    private String path;
    private Optional<String> queryString;

    public URL(Protocol protocol, String host, String path, Optional<String> queryString) {
        this.protocol = protocol;
        this.host = host;
        this.path = path;
        this.queryString = queryString;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public Optional<String> getQueryString() {
        return queryString;
    }
}
