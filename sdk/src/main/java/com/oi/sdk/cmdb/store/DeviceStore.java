package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.Lookup;
import com.oi.sdk.cmdb.entity.Device;
import com.oi.sdk.cmdb.entity.DeviceObject;
import com.oi.sdk.cmdb.entity.Group;
import com.oi.sdk.GlobalConstants;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class DeviceStore extends AbstractStore {

    private DeviceStore() {
    }

    private static DeviceStore INSTANCE;

    public static DeviceStore get() {
        if (INSTANCE == null) {
            INSTANCE = new DeviceStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.DEVICE.getName();
    }

    @Override
    public Device entity() {
        return new Device();
    }

    public Future<List<JsonObject>> fetch(List<Long> deviceIDs, List<Long> objectIDs) {

        JsonArray pipeline = new JsonArray();

        pipeline.add(Lookup.match(deviceIDs));

        pipeline.add(JsonObject.of("$lookup",
                JsonObject.of("from", ConfigCollection.OBJECT.getName())
                        .put("localField", GlobalConstants.ID)
                        .put("foreignField", DeviceObject.DEVICE_ID)
                        .put("as", Device.OBJECTS)));

        pipeline.add(JsonObject.of("$addFields",
                JsonObject.of(Device.OBJECTS,
                        JsonObject.of("$filter",
                                JsonObject.of("input", "$" + Device.OBJECTS)
                                        .put("as", "temp")
                                        .put("cond", JsonObject.of("$in", JsonArray.of("$$temp." + GlobalConstants.ID, objectIDs)))))));

        pipeline.add(JsonObject.of("$lookup",
                JsonObject.of("from", ConfigCollection.CREDENTIAL_PROFILE.getName())
                        .put("localField", Device.CREDENTIAL_PROFILES)
                        .put("foreignField", GlobalConstants.ID)
                        .put("as", Device.CREDENTIAL_PROFILES)));

        pipeline.add(JsonObject.of("$lookup",
                JsonObject.of("from", ConfigCollection.CREDENTIAL_PROFILE.getName())
                        .put("localField", Device.VALID_CREDENTIAL_PROFILE)
                        .put("foreignField", GlobalConstants.ID)
                        .put("as", Device.VALID_CREDENTIAL_PROFILE)));

        return repository.aggregation(collection(), pipeline);
    }

    public Future<JsonObject> fetch(Long id) {

        JsonArray pipeline = new JsonArray();

        pipeline.add(Lookup.match(id));

        pipeline.add(JsonObject.of("$lookup",
                JsonObject.of("from", ConfigCollection.CREDENTIAL_PROFILE.getName())
                        .put("localField", Device.CREDENTIAL_PROFILES)
                        .put("foreignField", GlobalConstants.ID)
                        .put("as", Device.CREDENTIAL_PROFILES)));

        pipeline.add(JsonObject.of("$lookup",
                JsonObject.of("from", ConfigCollection.CREDENTIAL_PROFILE.getName())
                        .put("localField", Device.VALID_CREDENTIAL_PROFILE)
                        .put("foreignField", GlobalConstants.ID)
                        .put("as", Device.VALID_CREDENTIAL_PROFILE)));

        return repository.aggregation(collection(), pipeline).map(values -> values != null && !values.isEmpty() ? values.getFirst() : null);
    }

    @SuppressWarnings("unchecked")
    public Future<List<Long>> groups(Long id) {

        JsonArray pipeline = new JsonArray();

        pipeline.add(Lookup.match(id));

        pipeline.add(JsonObject.of("$lookup", JsonObject.of("from", ConfigCollection.GROUP.getName())
                .put("localField", Device.GROUPS)
                .put("foreignField", GlobalConstants.ID)
                .put("as", "_groups")));

        pipeline.add(JsonObject.of("$graphLookup", JsonObject.of("from", ConfigCollection.GROUP.getName())
                .put("startWith", "$_groups." + Group.PARENT_ID)
                .put("connectFromField", "_groups." + Group.PARENT_ID)
                .put("connectToField", GlobalConstants.ID)
                .put("as", "_parents")));

        pipeline.add(JsonObject.of("$project",
                JsonObject.of(Device.GROUPS,
                        JsonObject.of("$setUnion", JsonArray.of("$_parents." + GlobalConstants.ID, "$" + Device.GROUPS)))));

        return repository.aggregation(collection(), pipeline).map(values -> values != null && !values.isEmpty() ? (List<Long>) values.getFirst().getJsonArray(Device.GROUPS).getList() : null);
    }
}
