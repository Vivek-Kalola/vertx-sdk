package com.oi.sdk.cmdb;

public class ConfigDBConstants {

    public static final String VERSION = "version";
    public static final String COLLECTION = "collection";
    public static final String ENTRIES = "entries";

    public static final String TRANSIENT_FIELDS = "transient_fields";


    public enum CreatedBy {
        SYSTEM,
        USER;

        public int get() {
            return this.ordinal();
        }
    }
}
