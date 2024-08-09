package com.oi.sdk.eventbus;

public enum RequestAddress implements Address {

    DEVICE_AVAILABILITY;

    @Override
    public String address() {
        return "REQUEST_" + name();
    }
}
