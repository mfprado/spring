package com.mfprado.server.handler.reader;

public class RequestReaderFactory {

    public RequestReader createRequestReader() {
        var methodParser = new MethodParser();
        var headersParser = new HeadersParser();
        var bodyParser = new BodyParser();
        var urlParser = new URLParser();
        return new RequestReader(methodParser, headersParser, bodyParser, urlParser);
    }
}
