package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.Lookup;
import com.oi.sdk.cmdb.entity.Device;
import com.oi.sdk.cmdb.entity.Group;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.oi.sdk.GlobalConstants.ID;

public class GroupStore extends AbstractStore {

    private GroupStore() {
    }

    private static GroupStore INSTANCE;

    public static GroupStore get() {
        if (INSTANCE == null) {
            INSTANCE = new GroupStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.GROUP.getName();
    }

    @Override
    public Group entity() {
        return new Group();
    }

    @SuppressWarnings("unchecked")
    public Future<Set<Long>> fetchAllDevices(List<Long> ids) {

        JsonArray pipeline = new JsonArray();

        pipeline.add(Lookup.match(ids));

        pipeline.add(JsonObject.of("$graphLookup",
                JsonObject.of("from", ConfigCollection.GROUP.getName())
                        .put("startWith", "$" + ID)
                        .put("connectFromField", ID)
                        .put("connectToField", Group.PARENT_ID)
                        .put("as", Group.CHILDREN)));

        pipeline.add(JsonObject.of("$project",
                JsonObject.of(Group.CHILDREN,
                        JsonObject.of("$concatArrays", JsonArray.of(JsonArray.of(("$_id"), "$" + Group.CHILDREN + "." + ID))))));

        pipeline.add(JsonObject.of("$lookup",
                JsonObject.of("from", ConfigCollection.DEVICE.getName())
                        .put("localField", Group.CHILDREN)
                        .put("foreignField", Device.GROUPS)
                        .put("as", Group.DEVICES)));

        pipeline.add(JsonObject.of("$project",
                JsonObject.of(Group.DEVICES, "$" + Group.DEVICES + "." + ID)
                        .put(Group.CHILDREN, 1)));

        return repository.aggregation(collection(), pipeline)
                .map(groups -> {

                    Set<Long> unique = new HashSet<>();

                    for (JsonObject group : groups) {
                        unique.addAll((List<Long>) group.getJsonArray(Group.DEVICES).getList());
                    }

                    return unique;
                });
    }

    @SuppressWarnings("unchecked")
    public Future<Set<Long>> fetchAllChildren(List<Long> ids) {

        JsonArray pipeline = new JsonArray();

        pipeline.add(Lookup.match(ids));

        pipeline.add(JsonObject.of("$graphLookup",
                JsonObject.of("from", ConfigCollection.GROUP.getName())
                        .put("startWith", "$" + ID)
                        .put("connectFromField", ID)
                        .put("connectToField", Group.PARENT_ID)
                        .put("as", Group.CHILDREN)));

        pipeline.add(JsonObject.of("$project",
                JsonObject.of(Group.CHILDREN,
                        JsonObject.of("$concatArrays", JsonArray.of(JsonArray.of(("$_id"), "$" + Group.CHILDREN + "." + ID))))));

        return repository.aggregation(collection(), pipeline)
                .map(groups -> {

                    Set<Long> unique = new HashSet<>();

                    for (JsonObject group : groups) {
                        unique.addAll((List<Long>) group.getJsonArray(Group.CHILDREN).getList());
                    }

                    return unique;
                });
    }
}
