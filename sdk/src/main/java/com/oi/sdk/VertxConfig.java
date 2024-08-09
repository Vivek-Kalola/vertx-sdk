package com.oi.sdk;

import io.vertx.core.json.JsonObject;

import java.nio.file.Files;
import java.nio.file.Path;

public class VertxConfig {

    private static final JsonObject CONFIG = new JsonObject();

    public static void init() {
        try {
            String content = Files.readString(Path.of(GlobalConstants.CONFIG_DIR + GlobalConstants.PATH_SEPARATOR + GlobalConstants.VERTX_CONFIG_FILE));
            CONFIG.mergeIn(new JsonObject(content));
        } catch (Exception ignored) {
        }
    }

    public static String getZookeeperHost() {
        return CONFIG.getString("zookeeper_host", "127.0.0.1:3181");
    }

}
