package com.mfprado.server.http;

import java.util.Map;
import java.util.Optional;

public enum  HttpResponses {
    SERVICE_UNAVAILABLE(new HttpResponse(HttpStatus.SERVICE_UNAVAILABLE, Map.of(), Optional.empty())),
    INTERNAL_SERVER_ERROR(new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, Map.of(), Optional.empty())),
    BAD_REQUEST(new HttpResponse(HttpStatus.BAD_REQUEST, Map.of(), Optional.empty()));

    private HttpResponse httpResponse;

    HttpResponses(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }
}
