package com.oi.sdk.logger;

import com.oi.sdk.logger.async.AsyncLogger;
import com.oi.sdk.logger.file.FileLogger;
import io.vertx.core.Vertx;

public class LoggerFactory {

    public static Logger getLogger(Module module, Class<?> clazz) {
        return new FileLogger(module, clazz);
    }

    public static Logger getAsyncLogger(Vertx vertx,Module module, Class<?> clazz) {
        return new AsyncLogger(vertx, module, clazz);
    }
}
