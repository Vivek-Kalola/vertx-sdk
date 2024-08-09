package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.Cluster;

public class ClusterStore extends AbstractStore {

    private ClusterStore() {
    }

    private static ClusterStore INSTANCE;

    public static ClusterStore get() {
        if (INSTANCE == null) {
            INSTANCE = new ClusterStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.CLUSTER.getName();
    }

    @Override
    public Cluster entity() {
        return new Cluster();
    }
}
