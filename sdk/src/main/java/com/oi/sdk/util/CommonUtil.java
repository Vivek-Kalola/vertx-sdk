package com.oi.sdk.util;

import com.oi.sdk.logger.Logger;
import com.oi.sdk.logger.LoggerFactory;
import com.oi.sdk.logger.Module;
import com.oi.sdk.GlobalConstants;
import com.oi.sdk.cmdb.store.GroupStore;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Vvek
 */
public class CommonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(Module.UTILS, CommonUtil.class);

    private static final AtomicLong ID_GENERATOR = new AtomicLong(System.nanoTime());

    public static long generateID() {
        return ID_GENERATOR.getAndIncrement();
    }

    private static final String MASK = "*";

    /**
     * should be used for sending the data. we don't want to display the sensitive data on client side.
     */
    public static JsonObject removeSensitiveFields(JsonObject context, boolean deep) {

        if (context == null || context.isEmpty()) {
            return new JsonObject();
        }

        try {

            JsonObject result = new JsonObject().mergeIn(context);

            for (String field : GlobalConstants.getSensitiveFields()) {

                if (context.containsKey(field)) {

                    if (deep && (result.getValue(field) instanceof JsonObject || result.getValue(field) instanceof Map)) {

                        result.put(field, removeSensitiveFields(result.getJsonObject(field), false));

                    } else {

                        result.remove(field);
                    }
                }
            }

            return result;

        } catch (Exception e) {
            LOGGER.error(e);
        }

        return context;
    }

    /**
     * Mask sensitive field, mostly used for auditing and logs
     */
    public static JsonObject maskSensitiveFields(JsonObject context, boolean deep) {

        if (context == null || context.isEmpty()) {
            return new JsonObject();
        }

        try {

            JsonObject result = new JsonObject().mergeIn(context);

            for (String field : GlobalConstants.getSensitiveFields()) {

                if (context.containsKey(field)) {

                    if (deep && (result.getValue(field) instanceof JsonObject || result.getValue(field) instanceof Map)) {

                        result.put(field, maskSensitiveFields(result.getJsonObject(field), false));

                    } else {

                        result.put(field, MASK.repeat(context.getValue(field).toString().length()));
                    }
                }
            }

            return result;

        } catch (Exception e) {
            LOGGER.error(e);
        }

        return context;
    }


    @SuppressWarnings(value = "unchecked")
    public static Future<Set<Long>> getDeviceIDs(JsonObject ctx) {

        Promise<Set<Long>> promise = Promise.promise();

        if (ctx != null) {

            if (GlobalConstants.EntityType.DEVICE.getName().equalsIgnoreCase(ctx.getString(GlobalConstants.ENTITY_TYPE))) {

                JsonArray entities = ctx.getJsonArray(GlobalConstants.ENTITIES);

                Set<Long> uniques = new HashSet<>();

                for (int i = 0; i < entities.size(); i++) {
                    uniques.add(entities.getLong(i));
                }

                promise.complete(uniques);

            } else if (GlobalConstants.EntityType.GROUP.getName().equalsIgnoreCase(ctx.getString(GlobalConstants.ENTITY_TYPE))) {

                JsonArray entities = ctx.getJsonArray(GlobalConstants.ENTITIES);

                if (entities != null && !entities.isEmpty()) {

                    GroupStore.get().fetchAllDevices((List<Long>) entities.getList())
                            .onSuccess(deviceIDs -> promise.complete(new HashSet<>(deviceIDs)))
                            .onFailure(e -> promise.complete(new HashSet<>()));
                }
            } else {
                promise.complete(new HashSet<>());
            }
        } else {
            promise.fail("empty context");
        }

        return promise.future();
    }
}
