package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.Entity;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class DiscoveryContextStore extends AbstractStore {

    private DiscoveryContextStore() {
    }

    private static DiscoveryContextStore INSTANCE;

    public static DiscoveryContextStore get() {
        if (INSTANCE == null) {
            INSTANCE = new DiscoveryContextStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.DISCOVERY_CONTEXT.getName();
    }

    @Override
    public Entity entity() {
        return new Entity() {
            @Override
            public Future<JsonObject> validate(JsonObject entity, boolean update) {
                return Future.succeededFuture(entity);
            }
        };
    }
}
