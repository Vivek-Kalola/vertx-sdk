package com.oi.sdk;

import java.util.Arrays;

/**
 * while interacting with Database we will be using these constants, as they are mapped to the database.
 * Transformation of different constants will happen while enriching the data.
 *

 */
public class ReportDBConstants {

    public static final String BATCHES = "batches";

    public static final String QUERY = "query";

    public static final String DATASOURCE = "datasource";

    public static final String DEVICE = "device";
    public static final String OBJECT = "object";
    public static final String POLICY = "policy";

    public static final String REQUEST_TYPE = "request_type";
    public static final String REQUEST_TYPE_GET = "request_type_get";
    public static final String REQUEST_TYPE_POST = "request_type_post";

    public static final String AVAILABILITY = "availability";

    public static final String INDICATOR = "indicator";
    public static final String INDICATOR_TYPE = "indicator_type";
    public static final String DATA_TYPE = "data_type";

    public enum Datasource {
        AUDIT("audit"),
        AVAILABILITY("availability"),
        FLOW("flow"),
        VIOLATION("violation"),
        ALERT("alert");

        private final String name;

        Datasource(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Datasource of(String name) {
            return Arrays.stream(values()).parallel().filter(e -> e.getName().equalsIgnoreCase(name)).findFirst().orElseThrow();
        }
    }
}
