package com.oi.sdk.cmdb;

import com.oi.sdk.audit.Audit;
import com.oi.sdk.GlobalConstants;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author Vvek
 */
public interface IConfigRepository {

    Future<Boolean> initCollections();

    Future<Void> init(Vertx vertx);

    Future<JsonArray> ids(String collection);

    Future<JsonArray> ids(String collection, JsonObject query);

    Future<Long> upsert(String collection, JsonObject document, Audit audit);

    Future<Long> saveAll(String collection, List<JsonObject> documents, Audit audit);

    Future<JsonObject> findOne(String collection, JsonObject query);

    default Future<JsonObject> findByID(String collection, long id) {
        return findOne(collection, JsonObject.of(GlobalConstants.ID, id));
    }

    default Future<List<JsonObject>> findAllByIDs(String collection, List<Long> ids) {
        return find(collection, JsonObject.of(GlobalConstants.ID, JsonObject.of("$in", ids)));
    }

    Future<List<JsonObject>> find(String collection, JsonObject query);

    /**
     * uses to perform aggregate query on Collection with given pipeline
     */
    Future<List<JsonObject>> aggregation(String collection, JsonArray pipeline);

    default Future<List<JsonObject>> findAll(String collection) {
        return find(collection, JsonObject.of());
    }

    Future<Long> update(String collection, long id, JsonObject document, Audit audit);

    Future<JsonObject> delete(String collection, long id, JsonObject query, Audit audit);

    Future<List<Long>> deleteAll(String collection, List<Long> ids, JsonObject query, Audit audit);

    // custom methods

    default JsonObject prepare(JsonObject document) {

        if (document.containsKey(ConfigDBConstants.TRANSIENT_FIELDS)) {

            JsonArray fields = document.getJsonArray(ConfigDBConstants.TRANSIENT_FIELDS);

            if (!fields.isEmpty()) {

                JsonObject copy = document.copy();

                for (int i = 0; i < fields.size(); i++) {
                    copy.remove(fields.getString(i));
                }

                copy.remove(ConfigDBConstants.TRANSIENT_FIELDS);

                return copy;
            }
        }

        return document;
    }
}