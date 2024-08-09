package com.oi.sdk;

import com.oi.sdk.logger.LogLevel;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;

public abstract class SystemConfig {

    protected final JsonObject CONFIG = new JsonObject();

    public enum Environment {
        DEV,
        TEST,
        UAT,
        PROD;

        public static Environment of(String name) {
            return Arrays.stream(values()).parallel().filter(e -> e.name().equalsIgnoreCase(name)).findFirst().orElse(DEV);
        }
    }

    public void init(JsonObject config) {
        CONFIG.clear();
        CONFIG.mergeIn(config);
    }

    public void onChange(JsonObject config) {
        CONFIG.clear();
        CONFIG.mergeIn(config);
    }

    protected abstract String module();

    public final String getModuleID() {
        return CONFIG.getString("module_id", module());
    }

    public final String getConfigDBURL() {
        return CONFIG.getString("config_db_url", "mongodb://localhost:27017");
    }

    public final String getConfigDB() {
        return CONFIG.getString("config_db", "aiops-cmdb");
    }

    public final int getLogLevel() {
        return CONFIG.getInteger("log_level", LogLevel.getDefault().level());
    }

    public final String getKafkaHost() {
        return CONFIG.getString("kafka_host", "localhost");
    }

    public final int getKafkaPort() {
        return CONFIG.getInteger("kafka_port", 9092);
    }

    public final int getDefaultAvailabilityInterval() {
        return CONFIG.getInteger("default_availability_interval_sec", 60);
    }

    public final int getDefaultPollingInterval() {
        return CONFIG.getInteger("default_polling_interval_sec", 300);
    }

    public final boolean devEnabled() {
        return Environment.DEV == getEnvironment();
    }

    public final Environment getEnvironment() {
        return Environment.valueOf(this.CONFIG.getString("environment", Environment.DEV.name()));
    }
}
