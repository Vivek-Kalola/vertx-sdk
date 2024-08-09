package com.oi.sdk.eventbus;

public interface Address {

    String address();

    default String address(String id) {
        return address() + "_" + id;
    }

    default String reply() {
        return address() + "_REPLY";
    }

    default String reply(String id) {
        return address(id) + "_REPLY";
    }

    default String response() {
        return address() + "_RESPONSE";
    }

    default String response(String id) {
        return address(id) + "_RESPONSE";
    }
}
