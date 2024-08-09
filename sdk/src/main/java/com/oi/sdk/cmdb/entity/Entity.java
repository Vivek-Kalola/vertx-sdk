package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.ConfigDBConstants;
import com.oi.sdk.cmdb.Lookup;
import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.FailedToDeleteException;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.cmdb.validation.Validator;
import com.oi.sdk.GlobalConstants;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

public interface Entity {

    @Field(dataType = DataType.NUMBER)
    String ID = GlobalConstants.ID;

    @Field(dataType = DataType.NUMBER)
    String TYPE = "_type";

    String CREATED_ON = "created_on";

    String CREATED_BY = "created_by";

    String UPDATED_ON = "updated_on";

    String UPDATED_BY = "updated_by";

    /**
     * reveres lookup on used reference
     */
    default List<Lookup> references() {
        return null;
    }

    /**
     * lookup on the foreign keys [LEFT_JOIN]
     */
    default List<Lookup> lookups() {
        return null;
    }

    default Future<JsonObject> validate(JsonObject entity, boolean update) {
        return Validator.validate(entity, this.getClass(), update);
    }

    /**
     * @throws FailedToDeleteException if entity delete validation has failed (primarily used for API Gateway)
     */
    default void validateDeleteViaAPI(JsonObject entity) throws FailedToDeleteException {

        JsonObject errors = new JsonObject();

        if (entity.getInteger(Entity.TYPE) == ConfigDBConstants.CreatedBy.SYSTEM.get()) {

            throw new FailedToDeleteException("system created entity");

        } else if (references() != null) {

            try {

                for (Lookup reference : references()) {

                    if (entity.getJsonArray(reference.as()) != null && !entity.getJsonArray(reference.as()).isEmpty()) {
                        errors.put(reference.as(), entity.getJsonArray(reference.as()));
                    }
                }

                if (!errors.isEmpty()) {
                    throw new FailedToDeleteException("references found", errors);
                }
            } catch (FailedToDeleteException e) {
                throw e;
            } catch (Exception e) {
                throw new FailedToDeleteException(e.getMessage());
            }
        }
    }

    default JsonObject addDefault(JsonObject entity) {
        return entity;
    }

    default void putIfAbsent(JsonObject entity, String key, Object value) {
        if (entity.getValue(key) == null) {
            entity.put(key, value);
        }
    }
}
