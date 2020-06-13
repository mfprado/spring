package com.mfprado.server.handler.reader;

import com.mfprado.server.handler.exception.InvalidMethodException;
import com.mfprado.server.http.HttpMethod;

import java.util.StringTokenizer;

public class MethodParser {

    public HttpMethod parse(String firstLine) throws InvalidMethodException {
        try {
            var tokenizer = new StringTokenizer(firstLine);
            return HttpMethod.valueOf(tokenizer.nextToken().toUpperCase());
        }
        catch (IllegalArgumentException e) {
            throw new InvalidMethodException();
        }
    }
}
