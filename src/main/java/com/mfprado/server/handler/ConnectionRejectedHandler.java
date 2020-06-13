package com.mfprado.server.handler;

import com.mfprado.server.handler.exception.WriterException;
import com.mfprado.server.handler.writer.ResponseWriter;
import com.mfprado.server.http.HttpResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

public class ConnectionRejectedHandler {
    private final Logger logger = LoggerFactory.getLogger(ConnectionRejectedHandler.class);
    private final ResponseWriter responseWriter;

    public ConnectionRejectedHandler(ResponseWriter responseWriter) {
        this.responseWriter = responseWriter;
    }

    public void reject(Socket socket) throws WriterException {
        logger.info("Connection rejected");
        responseWriter.write(socket, HttpResponses.SERVICE_UNAVAILABLE.getHttpResponse());
    }
}
