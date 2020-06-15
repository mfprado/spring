package com.mfprado.server;

import com.mfprado.server.exception.LifecycleException;
import com.mfprado.server.handler.ConnectionRejectedHandler;
import com.mfprado.server.handler.SocketHandler;
import com.mfprado.server.handler.exception.WriterException;
import com.mfprado.server.handler.reader.*;
import com.mfprado.server.handler.writer.ResponseWriter;
import com.mfprado.server.handler.writer.ResponseWriterFactory;
import com.mfprado.server.servlet.Servlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    private final Logger logger = LoggerFactory.getLogger(Server.class);
    private final Servlet servlet;
    private int port;
    private ThreadPoolExecutor threadPoolExecutor;

    public Server(int port, Servlet servlet, ThreadPoolExecutor threadPoolExecutor) {
        this.port = port;
        this.servlet = servlet;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void start() throws LifecycleException {
        try {
            var serverSocket = new ServerSocket(this.port);
            this.start(serverSocket);
        } catch (IOException e) {
            throw new LifecycleException("Cannot open port 8080", e);
        }
    }

    private void start(ServerSocket serverSocket) {
        var requestReader = createRequestReader();
        var responseWriter = createResponseWriter();
        var rejectHandler = new ConnectionRejectedHandler(responseWriter);
        while (true) {
            try {
                logger.info("Waiting client request");
                var socket = serverSocket.accept();
                logger.info("New client connected");
                try {
                    threadPoolExecutor.execute(new SocketHandler(socket, requestReader, responseWriter, servlet));
                } catch (RejectedExecutionException e) {
                    rejectHandler.reject(socket);
                    socket.close();
                }
            } catch (WriterException e) {
                logger.error("Error writing response", e);
            } catch (IOException e) {
                logger.error("Connection error", e);
            }
        }
    }

    private RequestReader createRequestReader() {
        return new RequestReaderFactory().createRequestReader();
    }

    private ResponseWriter createResponseWriter() {
        return new ResponseWriterFactory().createResponseWriter();
    }
}
