package com.oi.sdk.logger.console;

import com.oi.sdk.logger.LogLevel;
import com.oi.sdk.logger.Logger;

import java.util.Date;

public class ConsoleLogger implements Logger {

    private final Module module;
    private final Class<?> clazz;

    public ConsoleLogger(Module module, Class<?> clazz) {
        this.module = module;
        this.clazz = clazz;
    }

    private void log(String log) {
        System.out.println(log);
    }

    private void logError(String log) {
        System.err.println(log);
    }

    @Override
    public void trace(String log) {
        log(String.format("%s %s [%s:%s] %s", new Date(), LogLevel.TRACE.name(), this.module.getName(), this.clazz.getSimpleName(), log));
    }

    @Override
    public void debug(String log) {
        log(String.format("%s %s [%s:%s] %s", new Date(), LogLevel.DEBUG.name(), this.module.getName(), this.clazz.getSimpleName(), log));
    }

    @Override
    public void info(String log) {
        log(String.format("%s %s [%s:%s] %s", new Date(), LogLevel.INFO.name(), this.module.getName(), this.clazz.getSimpleName(), log));

    }

    @Override
    public void warn(String log) {
        log(String.format("%s %s [%s:%s] %s", new Date(), LogLevel.WARN.name(), this.module.getName(), this.clazz.getSimpleName(), log));
    }

    @Override
    public void error(Throwable throwable) {
        logError(String.format("%s %s [%s:%s] %s\n", new Date(), LogLevel.ERROR.name(), this.module.getName(), this.clazz.getSimpleName(), throwable.getMessage()));
        throwable.printStackTrace();
    }

    @Override
    public void error(String log, Throwable throwable) {
        log(String.format("%s %s [%s:%s] %s", new Date(), LogLevel.ERROR.name(), this.module.getName(), this.clazz.getSimpleName(), log));
        throwable.printStackTrace();
    }

    @Override
    public void fatal(String log) {
        logError(String.format("%s %s [%s:%s] %s", new Date(), LogLevel.FATAL.name(), this.module.getName(), this.clazz.getSimpleName(), log));
    }
}
