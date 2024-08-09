package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.DeviceManager;

public class DeviceManagerStore extends AbstractStore {

    private DeviceManagerStore() {
    }

    private static DeviceManagerStore INSTANCE;

    public static DeviceManagerStore get() {
        if (INSTANCE == null) {
            INSTANCE = new DeviceManagerStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.DEVICE_MANAGER.getName();
    }

    @Override
    public DeviceManager entity() {
        return new DeviceManager();
    }
}
