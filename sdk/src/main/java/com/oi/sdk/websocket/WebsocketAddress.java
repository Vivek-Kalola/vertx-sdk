package com.oi.sdk.websocket;

import com.oi.sdk.eventbus.Address;

public enum WebsocketAddress implements Address {

    SERVER_EVENT {
        @Override
        public String address() {
            return "~" + super.address();
        }
    },
    SESSION_EVENT {
        @Override
        public String address() {
            return "~" + super.address();
        }
    },
    NOTIFICATION_EVENT {
        @Override
        public String address() {
            return "~" + super.address();
        }
    },

    WS_TEST,
    WS_HEARTBEAT,

    WS_POLL_DEVICE,
    WS_ENABLE_FLOW,
    WS_DISABLE_FLOW,

    WS_AVAILABILITY,
    WS_AUDIT,
    WS_VISUALIZATION,
    WS_ALERT_HISTORICAL,
    WS_ALERT_LIVE,
    WS_INDICATOR_MAPPER;

    public String address() {
        return name();
    }
}

