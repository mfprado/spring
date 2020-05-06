package com.spring.concurrency;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.*;


public class ServerThreadPoolExecutor {
    private static final String PROPERTIES_FILE = "/conf/app/thread.pool.executor.properties";
    private static int CORE_POOL_SIZE = 256;
    private static int MAXIMUM_POOL_SIZE = 256;
    private static long KEEP_ALIVE_TIME = 60000;
    private static int QUEUE_CAPACITY = 10;

    static {
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final static ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(QUEUE_CAPACITY), new ThreadPoolExecutor.AbortPolicy());

    public static ThreadPoolExecutor getExecutor() {
        return EXECUTOR;
    }

    private static void loadProperties() throws IOException {

        Properties properties = new Properties();
        properties.load(ServerThreadPoolExecutor.class.getResourceAsStream(PROPERTIES_FILE));
        CORE_POOL_SIZE = Integer.parseInt((String) properties.get("corePoolSize"));
        MAXIMUM_POOL_SIZE = Integer.parseInt((String) properties.get("maximumPoolSize"));
        KEEP_ALIVE_TIME = Long.parseLong((String) properties.get("keepAliveTime"));
        QUEUE_CAPACITY = Integer.parseInt((String) properties.get("queueCapacity"));
    }
}
