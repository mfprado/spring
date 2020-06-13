package com.mfprado.server.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.*;


public class ServerThreadPoolExecutor {

    private static final Logger logger = LoggerFactory.getLogger(ServerThreadPoolExecutor.class);

    private static final String PROPERTIES_FILE = "/conf/app/thread.pool.executor.properties";
    private static int CORE_POOL_SIZE = 256;
    private static int MAXIMUM_POOL_SIZE = 256;
    private static long KEEP_ALIVE_TIME = 60000;

    static {
        try {
            loadProperties();
        } catch (Exception e) {
            logger.info("Cannot read thread pool executor properties from /conf/app");
            e.printStackTrace();
        }
    }

    private final static ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.MILLISECONDS,
            new SynchronousQueue());

    public static ThreadPoolExecutor getExecutor() {
        return EXECUTOR;
    }

    private static void loadProperties() throws IOException {

        Properties properties = new Properties();
        properties.load(ServerThreadPoolExecutor.class.getResourceAsStream(PROPERTIES_FILE));
        CORE_POOL_SIZE = Integer.parseInt((String) properties.get("corePoolSize"));
        MAXIMUM_POOL_SIZE = Integer.parseInt((String) properties.get("maximumPoolSize"));
        KEEP_ALIVE_TIME = Long.parseLong((String) properties.get("keepAliveTime"));
    }
}
