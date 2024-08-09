package com.oi.sdk.cmdb.store;

import com.oi.sdk.audit.Audit;
import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.DeviceObject;
import com.oi.sdk.logger.Logger;
import com.oi.sdk.logger.LoggerFactory;
import com.oi.sdk.logger.Module;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class DeviceObjectStore extends AbstractStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(Module.STORE, DeviceObjectStore.class);

    private DeviceObjectStore() {
    }

    private static DeviceObjectStore INSTANCE;

    public static DeviceObjectStore get() {
        if (INSTANCE == null) {
            INSTANCE = new DeviceObjectStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.OBJECT.getName();
    }

    @Override
    public DeviceObject entity() {
        return new DeviceObject();
    }

    public void deleteAllByDevice(long deviceID) {

        objectIDs(deviceID).onSuccess(ids -> {
                    if (ids != null && !ids.isEmpty()) {
                        deleteAll(ids, Audit.system())
                                .onSuccess(e -> LOGGER.info("Object removed for device id: " + deviceID))
                                .onFailure(LOGGER::error);
                    }
                })
                .onFailure(LOGGER::error);
    }

    public Future<List<JsonObject>> objects(long deviceID) {
        return this.findAllByFieldEqualsValue(DeviceObject.DEVICE_ID, deviceID);
    }

    public Future<List<Long>> objectIDs(long deviceID) {
        return this.idsByFieldEqualsValue(DeviceObject.DEVICE_ID, deviceID);
    }
}
