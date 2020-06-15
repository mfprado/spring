package com.mfprado.server.handler;

import com.mfprado.server.handler.exception.InvalidRequestException;
import com.mfprado.server.handler.exception.ReaderException;
import com.mfprado.server.handler.exception.WriterException;
import com.mfprado.server.handler.writer.ResponseWriter;
import com.mfprado.server.http.HttpResponse;
import com.mfprado.server.handler.reader.RequestReader;
import com.mfprado.server.http.HttpResponses;
import com.mfprado.server.servlet.Servlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class SocketHandler implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(SocketHandler.class);
    private final Socket socket;
    private RequestReader requestReader;
    private ResponseWriter responseWriter;
    private final Servlet dispatcher;

    public SocketHandler(Socket socket, RequestReader requestReader, ResponseWriter responseWriter, Servlet dispatcher) {
        this.socket = socket;
        this.requestReader = requestReader;
        this.responseWriter = responseWriter;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        try {
            try {
                var request = requestReader.read(socket);
                var response = new HttpResponse();
                dispatcher.dispatch(request, response);
                responseWriter.write(socket, response);
            } catch (ReaderException e) {
                logger.error("Error reading socket", e);
                handleUnexpectedError();
            } catch (InvalidRequestException e) {
                responseWriter.write(socket, HttpResponses.BAD_REQUEST.getHttpResponse());
            } finally {
                socket.close();
            }

        } catch (IOException e) {
            logger.error("Error handling socket: ", e);
        } catch (WriterException e) {
            logger.error("Error writing socket", e);
        }
    }

    private void handleUnexpectedError() throws WriterException {
        this.responseWriter.write(socket, HttpResponses.INTERNAL_SERVER_ERROR.getHttpResponse());
    }
}
