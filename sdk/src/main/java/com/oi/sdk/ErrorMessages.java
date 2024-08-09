package com.oi.sdk;

/**
 * @author Vvek
 */
public class ErrorMessages {

    public static final String FAILED_TO_START_ENGINE = "Failed to start engine, reason: %s";
    public static final String FAILED_TO_INITIALIZE_COLLECTION = "Failed to initialize collection [%s], reason: %s";
    public static final String FAILED_TO_INITIALIZE_STORE = "Failed to initialize store [%s], reason: %s";

    public static final String FAILED_TO_CREATE_ENTITY = "Failed to create entity, reason: %s";
    public static final String FAILED_TO_UPDATE_ENTITY = "Failed to update entity, reason: %s";

    public static final String FAILED_TO_DELETE_ENTITY = "Failed to delete entity, reason: %s";
    public static final String FAILED_TO_DELETE_IN_USE = "Failed to delete %s, reason: in use by one or more [%s]";

    public static final String INTERNAL_SERVER_ERROR = "Internal server error, reason: %s";

    public static final String LOGIN_FAILED = "Failed to login, reason %s";

    public static final String FAILED_TO_ENABLE_MONITORING = "Failed to enable Monitoring, reason: %s";
    public static final String FAILED_TO_DISABLE_MONITORING = "Failed to disable Monitoring, reason: %s";
    public static final String FAILED_TO_DISABLE_DEVICE = "Failed to disable Device, reason: %s";
    public static final String FAILED_TO_ENABLE_DEVICE = "Failed to enable Device, reason: %s";

    public static final String FAILED_TO_QUEUE_DISCOVERY = "failed to queue discovery for request %s, Reason: %s";
    public static final String FAILED_TO_QUEUE_POLLING = "failed to queue metric polling for request %s, Reason: %s";

    public static final String NOT_FOUND = "%s not found";
}
