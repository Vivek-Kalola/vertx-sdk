package com.oi.sdk.logger;

public enum Module {

    ALERT("alert"),
    API("api"),
    AUDIT("audit"),
    CONFIG("config"),
    CLUSTER("cluster"),
    DRUID("druid"),
    ENRICHER("enricher"),
    FLOW("flow"),
    HEALTH("health"),
    JOB("job"),
    KAFKA("kafka"),
    MONITORING("monitoring"),
    NOTIFICATION("notification"),
    PLUGIN("plugin"),
    REPORT_DB("report-db"),
    READER("reader"),
    WRITER("writer"),
    ROUTER("router"),
    STORE("store"),
    SYSTEM("system"),
    SERVER("server"),
    UTILS("utils"),
    EXPLORER("explorer"),
    VISUALIZATION("visualization"),
    WEBSOCKET("websocket");

    private final String name;

    Module(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
