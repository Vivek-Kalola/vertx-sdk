package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.Dashboard;

public class DashboardStore extends AbstractStore {

    private DashboardStore() {
    }

    private static DashboardStore INSTANCE;

    public static DashboardStore get() {
        if (INSTANCE == null) {
            INSTANCE = new DashboardStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.DASHBOARD.getName();
    }

    @Override
    public Dashboard entity() {
        return new Dashboard();
    }
}
