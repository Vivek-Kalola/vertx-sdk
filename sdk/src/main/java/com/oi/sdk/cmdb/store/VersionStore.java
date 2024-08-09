package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.Entity;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class VersionStore extends AbstractStore {

    private static VersionStore INSTANCE;

    public static VersionStore get() {
        if (INSTANCE == null) {
            INSTANCE = new VersionStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.VERSION.getName();
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
