package com.oi.sdk.logger.async;

import com.oi.sdk.logger.LogLevel;
import com.oi.sdk.logger.Logger;
import com.oi.sdk.logger.Module;
import io.vertx.core.Vertx;

/**
 * tbd
 * will be developed after 2.1 if we observe the system level lagging
 */
public class AsyncLogger implements Logger {

    private final Vertx vertx;
    private final Module module;
    private final Class<?> clazz;

    public static final String EVENT_LOG_ADDRESS = "event.async.log.address";

    public record LogMessage(LogLevel level, Module module, Class<?> clazz, String log) {
    }

    public AsyncLogger(Vertx vertx, Module module, Class<?> clazz) {
        this.vertx = vertx;
        this.module = module;
        this.clazz = clazz;
    }

    @Override
    public void trace(String log) {
        vertx.eventBus().send(EVENT_LOG_ADDRESS, new LogMessage(LogLevel.TRACE, module, clazz, log));
    }

    @Override
    public void debug(String log) {

    }

    @Override
    public void info(String log) {

    }

    @Override
    public void warn(String log) {

    }

    @Override
    public void error(Throwable throwable) {

    }

    @Override
    public void error(String log, Throwable throwable) {

    }

    @Override
    public void fatal(String log) {

    }
}
