package com.oi.sdk.logger;

public enum LogLevel {

    FATAL,  // 0
    ERROR,  // 1
    WARN,   // 2
    INFO,   // 3
    DEBUG,  // 4
    TRACE;  // 5

    public static LogLevel getDefault() {
        return ERROR;
    }

    public int level() {
        return ordinal();
    }
}