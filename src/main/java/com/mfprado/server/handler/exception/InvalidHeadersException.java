package com.mfprado.server.handler.exception;

public class InvalidHeadersException extends ParserException {
    public InvalidHeadersException() {
        super("Error parsing headers");
    }
}
