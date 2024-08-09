package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.CredentialProfile;

public class CredentialProfileStore extends AbstractStore {

    private CredentialProfileStore() {
    }

    private static CredentialProfileStore INSTANCE;

    public static CredentialProfileStore get() {
        if (INSTANCE == null) {
            INSTANCE = new CredentialProfileStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.CREDENTIAL_PROFILE.getName();
    }

    @Override
    public CredentialProfile entity() {
        return new CredentialProfile();
    }
}
