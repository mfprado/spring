package com.mfprado.server.handler.writer;

import com.mfprado.server.handler.exception.WriterException;
import com.mfprado.server.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ResponseWriter {

    private final Logger logger = LoggerFactory.getLogger(ResponseWriter.class);
    private ResponseParser parser;

    public ResponseWriter(ResponseParser parser) {
        this.parser = parser;
    }

    public void write(Socket socket, HttpResponse httpResponse) throws WriterException {
        try {
            var response = parser.parse(httpResponse);
            write(socket.getOutputStream(), response);
        } catch (IOException e) {
            logger.error("Error writing socket", e);
            throw new WriterException("Error writing socket", e);
        }
    }

    private void write(OutputStream output, String response) {
        var printWriter = new PrintWriter(output, true);
        printWriter.println(response);
    }
}
