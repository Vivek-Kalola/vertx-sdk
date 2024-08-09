package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.AuthToken;

public class AuthTokenStore extends AbstractStore {

    private AuthTokenStore() {
    }

    private static AuthTokenStore INSTANCE;

    public static AuthTokenStore get() {
        if (INSTANCE == null) {
            INSTANCE = new AuthTokenStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.AUTH_TOKEN.getName();
    }

    @Override
    public AuthToken entity() {
        return new AuthToken();
    }
}
