package com.oi.sdk;

import com.oi.sdk.cmdb.entity.User;

import java.nio.file.FileSystems;
import java.util.Arrays;

/**
 * @author Vvek
 */
public final class GlobalConstants {

    /**
     * Current Working Directory
     */
    public static final String CWD = System.getProperty("user.dir");

    public static final String PATH_SEPARATOR = FileSystems.getDefault().getSeparator();

    public static final String NEW_LINE = System.lineSeparator();

    public static final String CONFIG_DIR = CWD + PATH_SEPARATOR + "config";

    public static final String SAMPLE_FILES = "sample-files";
    public static final String SAMPLE_FILES_DIR = CWD + PATH_SEPARATOR + SAMPLE_FILES;

    public static final String UPLOADS = "uploads";
    public static final String UPLOADS_DIR = CWD + PATH_SEPARATOR + UPLOADS;

    public static final String DOWNLOADS = "downloads";
    public static final String DOWNLOADS_DIR = CWD + PATH_SEPARATOR + DOWNLOADS;

    public static final String METADATA = "_metadata";
    public static final String METADATA_DIR = CWD + PATH_SEPARATOR + METADATA;

    public static final String SYSTEM_CONFIG_FILE = "system.config";
    public static final String VERTX_CONFIG_FILE = "vertx.config";

    public static final String PLUGIN_ENGINE = "plugin-engine";
    public static final String PLUGIN_ENGINE_DIR = CWD + PATH_SEPARATOR + PLUGIN_ENGINE;
    public static final String PLUGIN_CONTEXT_DIR = PLUGIN_ENGINE_DIR + PATH_SEPARATOR + "contexts";

    public static final String ID = "_id";  // synced with MongoDB
    public static final String TENANT_ID = "tenant_id";
    public static final String CLUSTER_ID = "cluster_id";
    public static final String DEVICE_ID = "device_id";

    public static final long DEFAULT_ID = 1000000000001L;

    public static final String SYSTEM_USER = "system";
    public static final String DEFAULT = "default";
    public static final String UNKNOWN = "unknown";

    public static final String INSTALLATION_DATE = "installation_date";

    public static final String STATUS = "status";
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAIL = "fail";
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_CLEAR = "clear";
    public static final String STATUS_NONE = "none";

    public static final String YES = "yes";
    public static final String NO = "no";
    public static final String OBJECTS = "objects";
    public static final String OBJECT = "object";
    public static final String OBJECT_TYPE = "object_type";
    public static final String OBJECT_TYPES = "object_types";

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String EVENT_TYPE = "event_type";
    public static final String ENTITY_TYPE = "entity_type";
    public static final String ENTITIES = "entities";

    public static final String MESSAGE = "message";
    public static final String CONTEXT = "context";
    public static final String ERROR = "error";
    public static final String ERRORS = "errors";
    public static final String RESULT = "result";
    public static final String RESPONSE = "response";
    public static final String EXIT_CODE = "exit_code";
    public static final String TIMESTAMP = "timestamp";
    public static final String RECEIVED_TIMESTAMP = "received_timestamp";
    public static final String START_TIMESTAMP = "start_timestamp";
    public static final String END_TIMESTAMP = "end_timestamp";

    public static final String ENABLED = "enabled";
    public static final String DISABLED = "disabled";

    public static final String CSV_DELIMITER = "~";
    public static final String FILE = "file";

    public static final String READ = "read";
    public static final String WRITE = "write";
    public static final String DELETE = "delete";

    public static final String KAFKA_TOPIC_FLOW_INBOUND = "flow_inbound";

    public static final String REPLY_ADDRESS = "reply_address";

    public static String[] getSensitiveFields() {
        return new String[]{User.PASSWORD};
    }

    public enum EntityType {
        DEVICE("device"),
        GROUP("group"),
        SITE("site");

        private final String name;

        EntityType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static EntityType of(String name) {
            return Arrays.stream(values()).parallel().filter(e -> e.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        }
    }
}
