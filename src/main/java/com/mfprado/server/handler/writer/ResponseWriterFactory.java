package com.mfprado.server.handler.writer;

public class ResponseWriterFactory {

    public ResponseWriter createResponseWriter() {
        var parser = new ResponseParser();
        return new ResponseWriter(parser);
    }
}
