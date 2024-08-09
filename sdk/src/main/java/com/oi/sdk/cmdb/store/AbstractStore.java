package com.oi.sdk.cmdb.store;

import com.oi.sdk.audit.Audit;
import com.oi.sdk.cmdb.IConfigRepository;
import com.oi.sdk.cmdb.Lookup;
import com.oi.sdk.cmdb.entity.Entity;
import com.oi.sdk.Injector;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public abstract class AbstractStore {

    protected final IConfigRepository repository;

    protected final Entity entity;

    protected AbstractStore() {
        repository = Injector.get(IConfigRepository.class);
        entity = entity();
    }

    public abstract String collection();

    public abstract Entity entity();

    public Future<Long> upsert(JsonObject document, Audit audit) {
        return entity.validate(document, false)
                .compose(doc -> repository.upsert(collection(), doc, audit));
    }

    public Future<Long> update(long id, JsonObject document, Audit audit) {
        return entity.validate(document, true)
                .compose(doc -> repository.update(collection(), id, doc, audit));
    }

    @SuppressWarnings("unchecked")
    public Future<List<Long>> ids() {
        return repository.ids(collection()).map(r -> (List<Long>) r.getList());
    }

    public Future<JsonObject> find(long id) {
        return find(id, false);
    }

    public Future<JsonObject> find(long id, boolean references) {

        if (references) {
            return findAll(List.of(id), true).map(entities -> entities != null && !entities.isEmpty() ? entities.getFirst() : null);
        }

        return repository.findByID(collection(), id);
    }

    public Future<List<JsonObject>> findAll() {
        return findAll(false);
    }

    public Future<List<JsonObject>> findAll(boolean references) {

        if (references && entity.references() != null) {

            JsonArray pipeline = new JsonArray();

            for (Lookup reference : entity.references()) {
                pipeline.addAll(reference.reference());
            }

            return repository.aggregation(collection(), pipeline);
        }

        return repository.findAll(collection());
    }

    public Future<List<JsonObject>> findAll(List<Long> ids) {
        return findAll(ids, false);
    }

    public Future<List<JsonObject>> findAll(List<Long> ids, boolean references) {

        if (references && entity.references() != null) {

            JsonArray pipeline = new JsonArray();

            pipeline.add(Lookup.match(ids));

            for (Lookup reference : entity.references()) {
                pipeline.addAll(reference.reference());
            }

            return repository.aggregation(collection(), pipeline);
        }

        return repository.findAllByIDs(collection(), ids);
    }

    public <T> Future<List<JsonObject>> findAllByFieldEqualsValue(String field, T value) {
        return repository.find(collection(), JsonObject.of(field, value));
    }

    public <T> Future<JsonObject> findOneByFieldEqualsValue(String field, T value) {
        return repository.findOne(collection(), JsonObject.of(field, value));
    }

    public <T> Future<List<JsonObject>> findAllByFieldContainsValue(String field, T value) {
        return repository.find(collection(), JsonObject.of(field, JsonObject.of("$in", JsonArray.of(value))));
    }

    @SuppressWarnings("unchecked")
    public <T> Future<List<Long>> idsByFieldEqualsValue(String field, T value) {
        return repository.ids(collection(), JsonObject.of(field, value)).map(r -> (List<Long>) r.getList());
    }

    @SuppressWarnings("unchecked")
    public <T> Future<List<Long>> idsByFieldContainsValue(String field, T value) {
        return repository.ids(collection(), JsonObject.of(field, JsonObject.of("$in", JsonArray.of(value)))).map(r -> (List<Long>) r.getList());
    }

    @SuppressWarnings("unchecked")
    public <T> Future<List<Long>> idsByFieldContainsValue(String field, List<T> value) {
        return repository.ids(collection(), JsonObject.of(field, JsonObject.of("$in", value))).map(r -> (List<Long>) r.getList());
    }

    public Future<JsonObject> delete(long id, JsonObject query, Audit audit) {
        return repository.delete(collection(), id, query, audit);
    }

    public Future<JsonObject> delete(long id, Audit audit) {
        return this.delete(id, null, audit);
    }

    public Future<List<Long>> deleteAll(List<Long> ids, JsonObject query, Audit audit) {
        return repository.deleteAll(collection(), ids, query, audit);
    }

    public Future<List<Long>> deleteAll(List<Long> ids, Audit audit) {
        return this.deleteAll(ids, null, audit);
    }
}
