package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.Scheduler;

public class SchedulerStore extends AbstractStore {

    private SchedulerStore() {
    }

    private static SchedulerStore INSTANCE;

    public static SchedulerStore get() {
        if (INSTANCE == null) {
            INSTANCE = new SchedulerStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.SCHEDULER.getName();
    }

    @Override
    public Scheduler entity() {
        return new Scheduler();
    }
}
