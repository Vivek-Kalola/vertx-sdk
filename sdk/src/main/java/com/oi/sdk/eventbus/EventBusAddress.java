package com.oi.sdk.eventbus;

public enum EventBusAddress implements Address {

    // Engine life cycle
    BOOT_ENGINE,
    REBOOT_ENGINE,
    SHUTDOWN_ENGINE,

    // Polling / Plugin engine
    POLLING_REQUEST,
    PLUGIN_RESPONSE_PROCESSOR,

    // Availability
    AVAILABILITY,
    DEVICE_AVAILABILITY,
    DEVICE_MANAGER_AVAILABILITY,

    // Discovery
    IDENTIFICATION_RUN,

    // Discovery
    DISCOVERY_RUN,

    // Metric Enricher
    METRIC_ENRICHER,

    //Report DB Reader
    REPORT_DB_QUERY,
    REPORT_DB_QUERY_SQL,
    REPORT_DB_SUPERVISORS,
    REPORT_DB_HEALTH,
    REPORT_DB_REPLY,

    //Report DB Writer
    REPORT_DB_METRIC_WRITER,
    REPORT_DB_FLOW_WRITER,
    REPORT_DB_AVAILABILITY_WRITER,
    REPORT_DB_AUDIT_WRITER,
    REPORT_DB_ALERT_WRITER,
    REPORT_DB_ALARM_WRITER,
    REPORT_DB_PUBLISH_SUPERVISOR,

    // Druid Manager
    DRUID_METADATA_RESPONSE,

    // Flow
    FLOW,

    // Kafka
    KAFKA_PRODUCER,

    // Visualization
    VISUALIZATION,

    //Audit
    AUDIT,
    AUDIT_SEARCH,

    //Scheduler
    ADD_SCHEDULER,
    UPDATE_SCHEDULER,
    REMOVE_SCHEDULER,

    //Alert Engine
    INSPECT_ALERT,
    ALERT_VISUALIZATION,
    LIVE_ALERT_REQUEST,

    //Notification
    SEND_NOTIFICATION,
    ;

    public String address() {
        return name();
    }
}
