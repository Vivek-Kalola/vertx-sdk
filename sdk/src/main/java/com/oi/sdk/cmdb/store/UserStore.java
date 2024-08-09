package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.User;
import com.oi.sdk.GlobalConstants;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

public class UserStore extends AbstractStore {

    private UserStore() {
    }

    private static UserStore INSTANCE;

    public static UserStore get() {
        if (INSTANCE == null) {
            INSTANCE = new UserStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.USER.getName();
    }

    @Override
    public User entity() {
        return new User();
    }

    public Future<JsonObject> getUser(String username) {

        Promise<JsonObject> promise = Promise.promise();

        this.findOneByFieldEqualsValue(User.USERNAME, username)
                .onSuccess(user -> {

                    if (user != null && !user.isEmpty()) {
                        promise.complete(user);

                    } else {
                        promise.fail("User not found");
                    }
                })
                .onFailure(promise::fail);

        return promise.future();
    }

    public Future<JsonObject> validateUser(String username) {
        return getUser(username).compose(this::validateUser);
    }

    public Future<JsonObject> validateUser(JsonObject user) {
        if (GlobalConstants.YES.equalsIgnoreCase(user.getString(User.ENABLE))) {
            return Future.succeededFuture(user);
        } else {
            return Future.failedFuture("User is disabled");
        }
    }

}
