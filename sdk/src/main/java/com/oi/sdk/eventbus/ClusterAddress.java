package com.oi.sdk.eventbus;


public enum ClusterAddress implements Address {
    REGISTER_EVENT,
    HEARTBEAT,
    ;

    @Override
    public String address() {
        return "CLUSTER_" + name();
    }
}
