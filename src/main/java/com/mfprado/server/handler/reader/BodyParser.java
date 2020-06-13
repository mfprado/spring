package com.mfprado.server.handler.reader;

import com.mfprado.server.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BodyParser {
    private final Logger logger = LoggerFactory.getLogger(BodyParser.class);

    public Optional<String> parse(ServletInputStream servletInputStream, Map<String, List<String>> headers) {

        int contentLength = Optional.ofNullable(headers.get(HttpHeaders.CONTENT_LENGTH))
                .flatMap(l -> l.stream().findFirst())
                .map(Integer::valueOf)
                .orElse(0);

        if (contentLength > 0) return getBody(contentLength, servletInputStream);
        else return Optional.empty();
    }

    private Optional<String> getBody(int contentLength, ServletInputStream servletInputStream) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            int bytesRead;
            while ((bytesRead = servletInputStream.read()) > 0) {
                stringBuilder.append((char) bytesRead);
                if (stringBuilder.length() == contentLength) break;
            }

        } catch (Throwable e) {
            logger.error("Error reading request body", e);
            return Optional.empty();
        }
        return Optional.of(stringBuilder.toString());
    }
}
