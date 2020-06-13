package com.mfprado.server.handler.reader;

import com.mfprado.server.handler.exception.InvalidRequestException;
import com.mfprado.server.handler.exception.ParserException;
import com.mfprado.server.handler.exception.ReaderException;
import com.mfprado.server.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.text.ParseException;

public class RequestReader {
    private final Logger logger = LoggerFactory.getLogger(RequestReader.class);
    private final MethodParser methodParser;
    private final HeadersParser headersParser;
    private final BodyParser bodyParser;
    private final URLParser urlParser;

    public RequestReader(MethodParser methodParser, HeadersParser headersParser, BodyParser bodyParser, URLParser urlParser) {
        this.headersParser = headersParser;
        this.methodParser = methodParser;
        this.bodyParser = bodyParser;
        this.urlParser = urlParser;
    }

    private HttpRequest parseRequest(ServletInputStream servletInputStream) throws InvalidRequestException, IOException {
        try {
            var firstLine = servletInputStream.readLine();
            if (firstLine.equals("")) throw new InvalidRequestException("Empty request");

            var method = methodParser.parse(firstLine);
            var headers = headersParser.parse(servletInputStream);
            var url = urlParser.parse(firstLine, headers);
            var body = bodyParser.parse(servletInputStream, headers);

            return new HttpRequest(method, url, headers, body);

        } catch (ParserException e) {
            logger.error("Error parsing request");
            throw new InvalidRequestException(e.getMessage());
        }
    }

    public HttpRequest read(Socket socket) throws ReaderException, InvalidRequestException {
        logger.info("Reading request");
        try {
            var servletInputStream = new ServletInputStream(socket.getInputStream());
            return this.parseRequest(servletInputStream);
        } catch (IOException e) {
            throw new ReaderException("Error reading from socket", e);
        }
    }

}
