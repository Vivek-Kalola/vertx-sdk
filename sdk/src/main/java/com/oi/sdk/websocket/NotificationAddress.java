package com.oi.sdk.websocket;

import com.oi.sdk.eventbus.Address;

public enum NotificationAddress implements Address {

    TEST,
    AVAILABILITY_STATUS_CHANGE,
    DISCOVERY_PROGRESSION,
    DISCOVERY_RESULT,
    ALERT_STATE_CHANGE,
    CLUSTER_REGISTERED
    ;

    public String address() {
        return "NOTIFY_" + name();
    }
}

