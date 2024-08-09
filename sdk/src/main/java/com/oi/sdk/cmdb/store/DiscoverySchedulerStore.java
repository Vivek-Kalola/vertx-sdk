package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.DiscoveryScheduler;

public class DiscoverySchedulerStore extends AbstractStore {

    private DiscoverySchedulerStore() {
    }

    private static DiscoverySchedulerStore INSTANCE;

    public static DiscoverySchedulerStore get() {
        if (INSTANCE == null) {
            INSTANCE = new DiscoverySchedulerStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.DISCOVERY_SCHEDULER.getName();
    }

    @Override
    public DiscoveryScheduler entity() {
        return new DiscoveryScheduler();
    }
}
