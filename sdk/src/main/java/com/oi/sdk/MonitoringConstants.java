package com.oi.sdk;

import java.util.Arrays;

public class MonitoringConstants {

    public enum PluginType {
        SNMP,
        SSH,
        API;

        public String getName() {
            return name();
        }

        public static PluginType of(String value) {
            return Arrays.stream(values())
                    .parallel()
                    .filter(type -> type.name().equalsIgnoreCase(value))
                    .findFirst()
                    .orElseThrow();
        }
    }

    public enum RequestType {
        AVAILABILITY,
        IDENTIFICATION,
        DISCOVERY,
        POLLING;

        public static RequestType of(String value) {
            return Arrays.stream(values()).filter(e -> e.name().equalsIgnoreCase(value)).findFirst().orElseThrow();
        }
    }

    public static final String REQUEST_TYPE = "request_type";
    public static final String CONTEXTS = "contexts";
    public static final String AVAILABILITY_STATUS = "availability_status";
    public static final String AVAILABILITY_CONTEXT = "availability_context";
    public static final String ICMP_AVAILABILITY = "icmp_availability";
    public static final String PLUGIN_AVAILABILITY = "plugin_availability";
    public static final String AVAILABILITY_INTERVAL = "availability_interval";

    public static final String FLOW_ENABLED = "flow_enabled";

    // object
    public static final String SCALAR_OBJECT = "scalar";
    public static final String INTERFACE_OBJECT = "interface";

    public static final String BATCH_ID = "batch_id";

    public static final String RESPONSE_SEPARATOR = "§";
}