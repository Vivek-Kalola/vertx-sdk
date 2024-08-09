package com.oi.sdk.cmdb.validation;

import io.vertx.core.json.JsonObject;

public class FailedToDeleteException extends Exception {

    private final String message;
    private final JsonObject errors;

    public FailedToDeleteException(String message, JsonObject errors) {
        this.message = message;
        this.errors = errors;
    }

    public FailedToDeleteException(String message) {
        this(message, JsonObject.of());
    }

    public String message() {
        return "Failed to delete, Reason: " + message;
    }

    public JsonObject errors() {
        return errors;
    }
}
