package com.oi.sdk.logger;

import com.oi.sdk.Injector;
import com.oi.sdk.SystemConfig;

import java.io.PrintWriter;
import java.io.StringWriter;

public interface Logger {

    void trace(String log);

    void debug(String log);

    void info(String log);

    void warn(String log);

    void error(Throwable throwable);

    void error(String log, Throwable throwable);

    void fatal(String log);

    static boolean isTraceEnabled() {
        return Injector.get(SystemConfig.class).getLogLevel() >= LogLevel.TRACE.ordinal();
    }

    static boolean isDebugEnabled() {
        return Injector.get(SystemConfig.class).getLogLevel() >= LogLevel.DEBUG.ordinal();
    }

    static boolean isInfoEnabled() {
        return Injector.get(SystemConfig.class).getLogLevel() >= LogLevel.INFO.ordinal();
    }

    static boolean isWarnEnabled() {
        return Injector.get(SystemConfig.class).getLogLevel() >= LogLevel.WARN.ordinal();
    }

    static boolean isErrorEnabled() {
        return Injector.get(SystemConfig.class).getLogLevel() >= LogLevel.ERROR.ordinal();
    }

    static String formatStackTrace(Throwable cause) {

        StringBuilder sb = new StringBuilder();

        if (cause != null) {

            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                cause.printStackTrace(pw);
                pw.close();
                sb.append(sw);
            } catch (Exception ignored) {

            }
        }

        return sb.toString();
    }

}
