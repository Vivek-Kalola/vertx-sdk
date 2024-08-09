package com.oi.sdk.cmdb;

import com.oi.sdk.audit.Audit;
import com.oi.sdk.logger.Logger;
import com.oi.sdk.logger.LoggerFactory;
import com.oi.sdk.logger.Module;
import com.oi.sdk.util.CommonUtil;
import com.oi.sdk.util.DatetimeUtil;
import com.oi.sdk.Injector;
import com.oi.sdk.SystemConfig;
import com.oi.sdk.cmdb.entity.Entity;
import com.oi.sdk.cmdb.store.SystemStore;
import com.oi.sdk.cmdb.store.VersionStore;
import com.oi.sdk.cmdb.validation.Field;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.BulkOperation;
import io.vertx.ext.mongo.IndexOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientBulkWriteResult;
import io.vertx.ext.mongo.MongoClientDeleteResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.oi.sdk.GlobalConstants.CWD;
import static com.oi.sdk.GlobalConstants.DEFAULT_ID;
import static com.oi.sdk.GlobalConstants.ID;
import static com.oi.sdk.GlobalConstants.INSTALLATION_DATE;
import static com.oi.sdk.GlobalConstants.PATH_SEPARATOR;
import static com.oi.sdk.GlobalConstants.SYSTEM_USER;
import static io.vertx.core.Promise.promise;

public class MongoDBRepository implements IConfigRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(Module.CONFIG, MongoDBRepository.class);

    private Vertx vertx;

    private MongoClient client;

    @Override
    public Future<Void> init(Vertx vertx) {

        Promise<Void> promise = promise();

        SystemConfig systemConfig = Injector.get(SystemConfig.class);

        this.vertx = vertx;

        vertx.executeBlocking(() -> MongoClient.createShared(vertx, JsonObject.of("db_name", systemConfig.getConfigDB())
                        .put("connection_string", systemConfig.getConfigDBURL())))
                .onSuccess(c -> {
                    this.client = c;
                    promise.complete();
                }).onFailure(promise::fail);

        return promise.future();
    }

    @Override
    public Future<Boolean> initCollections() {

        return VersionStore.get().findAll()
                .compose(collections -> {
                    if (collections.isEmpty()) {
                        return createIndexes().map(e -> collections);
                    }
                    return Future.succeededFuture(collections);
                })
                .compose(collections -> {

                    List<Future<JsonObject>> tasks = new ArrayList<>();

                    JsonObject versions = JsonObject.of();

                    collections.forEach(c -> versions.put(c.getString(ConfigDBConstants.COLLECTION), c.getDouble(ConfigDBConstants.VERSION)));

                    for (ConfigCollection collection : ConfigCollection.values()) {

                        if (collection.loadDefaults()) {

                            double currentVersion = versions.getDouble(collection.getName(), 0.0);

                            tasks.add(vertx.executeBlocking(() -> {

                                try {

                                    JsonObject schema = new JsonObject(vertx.fileSystem().readFileBlocking(CWD + PATH_SEPARATOR + "config-schemas" + PATH_SEPARATOR + collection.getSchema()));

                                    if (schema.getDouble(ConfigDBConstants.VERSION, 1.0) > currentVersion) {
                                        return schema.put(ConfigDBConstants.COLLECTION, collection.getName())
                                                .put(ID, (long) collection.ordinal());
                                    }

                                } catch (Exception e) {
                                    LOGGER.error(e);
                                }
                                return null;
                            }));
                        }
                    }

                    return Future.join(tasks);
                })
                .compose(result -> {

                    List<JsonObject> schemas = result.list();

                    List<Future<Long>> updates = new ArrayList<>();

                    for (JsonObject obj : schemas) {

                        if (obj != null) {

                            JsonArray entries = obj.getJsonArray(ConfigDBConstants.ENTRIES);

                            List<JsonObject> documents = new ArrayList<>();

                            entries.forEach(e -> documents.add(((JsonObject) e).put(Entity.TYPE, ConfigDBConstants.CreatedBy.SYSTEM.get())
                                    .put(Entity.CREATED_ON, DatetimeUtil.now())
                                    .put(Entity.CREATED_BY, SYSTEM_USER)));

                            //todo: save all is not being validated by the validator
                            updates.add(saveAll(obj.getString(ConfigDBConstants.COLLECTION), documents, Audit.system())
                                    .compose(x -> VersionStore.get().upsert(JsonObject.of(ID, obj.getLong(ID))
                                            .put(ConfigDBConstants.COLLECTION, obj.getString(ConfigDBConstants.COLLECTION))
                                            .put(ConfigDBConstants.VERSION, obj.getDouble(ConfigDBConstants.VERSION)), Audit.system())));
                        }
                    }

                    return Future.join(updates)
                            .andThen(x -> SystemStore.get().upsert(JsonObject.of(ID, DEFAULT_ID).put(INSTALLATION_DATE, DatetimeUtil.nano()), Audit.system()))
                            .map(Future::succeeded);
                });
    }

    private Future<CompositeFuture> createIndexes() {

        List<Future<Void>> futures = new ArrayList<>();

        for (ConfigCollection collection : ConfigCollection.values()) {

            Class<? extends Entity> entity = collection.getEntity();

            if (entity != null) {

                Set<String> uniques = new HashSet<>();

                for (var field : entity.getFields()) {

                    try {

                        Field annotation = field.getAnnotation(Field.class);

                        if (annotation != null && annotation.unique()) {

                            field.setAccessible(true);

                            uniques.add(field.get(null).toString()); // null object to get the static fields
                        }

                    } catch (IllegalAccessException e) {
                        LOGGER.error(e);
                    }
                }

                futures.add(client.createCollection(collection.getName())
                        .andThen(x -> {
                            for (String field : uniques) {
                                futures.add(client.createIndexWithOptions(collection.getName(), JsonObject.of(field, 1), new IndexOptions().unique(true)));
                            }
                        }));
            }
        }

        return Future.join(futures);
    }

    @Override
    public Future<Long> upsert(String collection, JsonObject document, Audit audit) {

        boolean isUpdate = true;

        if (document.getLong(ID) == null) {
            document.put(ID, CommonUtil.generateID());
            isUpdate = false;
        }

        if (isUpdate) {
            audit.update(collection, document.getLong(ID), document);
        } else {
            audit.create(collection, document);
        }

        return client.save(collection, prepare(document)
                        .put(Entity.CREATED_BY, audit.username())
                        .put(Entity.CREATED_ON, DatetimeUtil.now()))
                .map(r -> document.getLong(ID))
                .map(x -> {
                    audit.publish(vertx);
                    return x;
                });
    }

    @Override
    public Future<Long> update(String collection, long id, JsonObject document, Audit audit) {

        audit.update(collection, id, document);

        return client.findOneAndUpdate(collection,
                        JsonObject.of(ID, id), JsonObject.of().put("$set", prepare(document)
                                .put(Entity.UPDATED_BY, audit.username())
                                .put(Entity.UPDATED_ON, DatetimeUtil.now())
                        ))
                .map(e -> id)
                .map(x -> {
                    audit.publish(vertx);
                    return x;
                });
    }

    @Override
    public Future<Long> saveAll(String collection, List<JsonObject> documents, Audit audit) {

        return client.bulkWrite(collection, documents.stream().map(d -> {
                    if (!d.containsKey(ID)) {
                        d.put(ID, CommonUtil.generateID());
                    }
                    return BulkOperation.createUpdate(JsonObject.of(ID, d.getLong(ID)), JsonObject.of("$set", prepare(d)), true, true);
                }).collect(Collectors.toList()))
                .map(MongoClientBulkWriteResult::getModifiedCount);
    }

    @Override
    public Future<JsonObject> findOne(String collection, JsonObject query) {
        return client.findOne(collection, query, null);
    }

    @Override
    public Future<List<JsonObject>> find(String collection, JsonObject query) {
        return client.find(collection, query);
    }

    @Override
    public Future<List<JsonObject>> aggregation(String collection, JsonArray pipeline) {

        Promise<List<JsonObject>> promise = promise();

        List<JsonObject> result = new ArrayList<>();

        client.aggregate(collection, pipeline)
                .endHandler(x -> promise.complete(result))
                .handler(result::add);

        return promise.future();
    }

    @Override
    public Future<JsonObject> delete(String collection, long id, JsonObject query, Audit audit) {

        audit.delete(collection, id);

        return client.findOneAndDelete(collection, (query == null ? JsonObject.of() : query).put(ID, id))
                .map(x -> {
                    audit.publish(vertx);
                    return x;
                });
    }

    @Override
    public Future<List<Long>> deleteAll(String collection, List<Long> ids, JsonObject query, Audit audit) {

        audit.delete(collection, ids);

        return client.removeDocuments(collection, (query == null ? JsonObject.of() : query)
                        .put(ID, JsonObject.of("$in", new JsonArray(ids))))
                .map(MongoClientDeleteResult::toJson)
                .map(x -> {
                    audit.publish(vertx);
                    return ids;
                });
    }

    @Override
    public Future<JsonArray> ids(String collection) {
        return client.distinct(collection, ID, Long.class.getCanonicalName());
    }

    @Override
    public Future<JsonArray> ids(String collection, JsonObject query) {
        return client.distinctWithQuery(collection, ID, Long.class.getCanonicalName(), query);
    }
}
