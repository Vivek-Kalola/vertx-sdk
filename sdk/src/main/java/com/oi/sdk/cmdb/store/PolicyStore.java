package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.Policy;

public class PolicyStore extends AbstractStore {

    private PolicyStore() {
    }

    private static PolicyStore INSTANCE;

    public static PolicyStore get() {
        if (INSTANCE == null) {
            INSTANCE = new PolicyStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.POLICY.getName();
    }

    @Override
    public Policy entity() {
        return new Policy();
    }
}
