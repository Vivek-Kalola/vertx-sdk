package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.Entity;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class SystemStore extends AbstractStore {

    private static SystemStore INSTANCE;

    public static SystemStore get() {
        if (INSTANCE == null) {
            INSTANCE = new SystemStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.SYSTEM.getName();
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
