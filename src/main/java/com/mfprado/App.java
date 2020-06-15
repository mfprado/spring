package com.mfprado;

import com.mfprado.server.concurrency.ServerThreadPoolExecutor;
import com.mfprado.server.Server;
import com.mfprado.server.exception.LifecycleException;
import com.mfprado.server.servlet.Servlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {
            var servlet = new Servlet();
            Server server = new Server(9090, servlet, ServerThreadPoolExecutor.getExecutor());
            server.start();
        } catch (LifecycleException e) {
            logger.error("Error starting server", e);
        }

        System.exit(-1);
    }
}
