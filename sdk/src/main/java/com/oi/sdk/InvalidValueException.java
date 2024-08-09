package com.oi.sdk;

public class InvalidValueException extends RuntimeException {
    public InvalidValueException(String field, String value) {
        super(String.format("%s has invalid value %s", field, value));
    }

    public InvalidValueException(String field, long value) {
        this(field, String.valueOf(value));
    }
}
