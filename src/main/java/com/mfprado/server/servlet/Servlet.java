package com.mfprado.server.servlet;

import com.mfprado.jdk.ServletRequest;
import com.mfprado.jdk.ServletResponse;
import com.mfprado.server.http.HttpHeaders;
import com.mfprado.server.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class Servlet implements com.mfprado.jdk.Servlet {
    private final Logger logger = LoggerFactory.getLogger(Servlet.class);

    @Override
    public void dispatch(ServletRequest request, ServletResponse response) {
        logger.info("dispatching");
        var body = "response body";
        response.setStatus(HttpStatus.OK);
        response.setBody(Optional.of(body));
        response.addHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
        response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(body.length()));
    }
}
