package com.mfprado.server.handler.exception;

public class InvalidRequestException extends Exception {
    public InvalidRequestException(String msg) {
        super("Invalid request: " +  msg);
    }
}
