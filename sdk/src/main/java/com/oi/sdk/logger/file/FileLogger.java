package com.oi.sdk.logger.file;

import com.oi.sdk.logger.LogLevel;
import com.oi.sdk.logger.Logger;
import com.oi.sdk.logger.Module;
import com.oi.sdk.GlobalConstants;
import io.vertx.core.impl.Utils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Vvek
 * Logger class for writing the logs to file
 * It has 2 properties, Module and Class
 */
public class FileLogger implements Logger {

    public static final String LOG_DIRECTORY = GlobalConstants.CWD + GlobalConstants.PATH_SEPARATOR + "logs";

    private static final SimpleDateFormat FILE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-");

    private static final SimpleDateFormat LOG_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private final Module module;

    private final Class<?> clazz;

    public FileLogger(Module module, Class<?> clazz) {
        this.module = module;
        this.clazz = clazz;
    }

    private void log(String log) {

        try {

            Path logDirectory = Path.of(LOG_DIRECTORY);

            if (!Files.exists(logDirectory))    // create log directory if deleted
            {
                Files.createDirectory(logDirectory);
            }

            Path directory = Path.of(LOG_DIRECTORY + GlobalConstants.PATH_SEPARATOR + module.getName());

            if (!Files.exists(directory)) {
                Files.createDirectory(directory);
            }

            Date currentDate = new Date();

            File file = new File(directory + GlobalConstants.PATH_SEPARATOR + FILE_FORMAT.format(currentDate) + clazz.getSimpleName() + ".log");

            FileUtils.writeStringToFile(file, String.format("%s %s\n", LOG_FORMAT.format(currentDate), log), StandardCharsets.UTF_8, true);

        } catch (Exception ignored) {

        }
    }

    @Override
    public void trace(String log) {
        if (Logger.isTraceEnabled()) {
            log(LogLevel.TRACE.name() + " " + log);
        }
    }

    @Override
    public void debug(String log) {
        if (Logger.isDebugEnabled()) {
            log(LogLevel.DEBUG + " " + log);
        }
    }

    @Override
    public void info(String log) {
        if (Logger.isInfoEnabled()) {
            log(LogLevel.INFO + " " + log);
        }
    }

    @Override
    public void warn(String log) {
        if (Logger.isWarnEnabled()) {
            log(LogLevel.WARN + " " + log);
        }
    }

    @Override
    public void error(Throwable error) {
        error("", error);
    }

    @Override
    public void error(String message, Throwable cause) {

        try {
            // Minimize memory allocations here.
            log("[" + Thread.currentThread().getName() + "] " +
                    LogLevel.ERROR.name() +
                    " [" + this.clazz.getCanonicalName() + "] " + message +
                    Utils.LINE_SEPARATOR +
                    formatStackTrace(cause));

        } catch (Exception ignored) {

        }
    }

    @Override
    public void fatal(String log) {
        log(LogLevel.FATAL + " " + log);
    }

    public static String formatStackTrace(Throwable cause) {

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
