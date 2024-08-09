package com.oi.sdk.audit;

import com.oi.sdk.GlobalConstants;

/**
 * @author Vvek
 */
public class AuditConstants {

    /**
     * Audit actions, with Success and Fail
     */
    public enum Action {

        CREATE("%s has been created successfully", "Failed to create %s"),
        UPDATE("%s has been updated successfully", "Failed to update %s"),
        DELETE("%s has been deleted successfully", "Failed to delete %s"),
        LOGIN("%s logged in successfully", "Login failed for username: %s"),
        LOGOUT("%s logged out successfully", "Logout failed for username: %s");

        private final String success;
        private final String fail;

        Action(String success, String fail) {
            this.success = success;
            this.fail = fail;
        }

        public String success(String param) {
            return String.format(success, param);
        }

        public String fail(String param) {
            return String.format(fail, param);
        }
    }

    public static final String REMOTE_HOST = "remote_host";
    public static final String USERNAME = "username";
    public static final String COLLECTION = "collection";
    public static final String ACTION = "action";
    public static final String STATUS = GlobalConstants.STATUS;
    public static final String ENTITY = "entity";
    public static final String MESSAGE = GlobalConstants.MESSAGE;
    public static final String IDS = "ids";
    public static final String UPDATED_ENTITY = "updated_entity";
}
