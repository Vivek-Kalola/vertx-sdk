package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.Role;

public class RoleStore extends AbstractStore {

    private RoleStore() {
    }

    private static RoleStore INSTANCE;

    public static RoleStore get() {
        if (INSTANCE == null) {
            INSTANCE = new RoleStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.ROLE.getName();
    }

    @Override
    public Role entity() {
        return new Role();
    }
}
