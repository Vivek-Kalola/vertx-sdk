package com.oi.sdk.eventbus;

public enum GlobalEventAddress implements Address {

    DEVICE_ADD,
    DEVICE_DELETE,
    DEVICE_DISABLE,

    POLICY_ADD,
    POLICY_UPDATE,
    POLICY_DELETE,

    GROUP_ADD,
    GROUP_DELETE,

    FLOW_ENABLED,
    FLOW_DISABLED,
    DEVICE_MONITORING_ENABLED,
    DEVICE_MONITORING_DISABLED,
    DISCOVERY_SUCCESS,
    OBJECT_UPDATE,
    SMTP_CONFIGURE_CHANGE;

    @Override
    public String address() {
        return "GLOBAL_" + name();
    }
}
