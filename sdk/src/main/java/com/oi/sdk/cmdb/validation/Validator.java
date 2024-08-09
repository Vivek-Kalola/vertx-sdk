package com.oi.sdk.cmdb.validation;

import com.oi.sdk.logger.Logger;
import com.oi.sdk.logger.LoggerFactory;
import com.oi.sdk.logger.Module;
import com.oi.sdk.cmdb.entity.Entity;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.regex.Pattern;

public class Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Module.CONFIG, Validator.class);

    // Ref: org.apache.commons.net.util.SubnetUtils
    public static final Pattern IP_ADDRESS_PATTERN = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");
    public static final Pattern CIDR_PATTERN = Pattern.compile(IP_ADDRESS_PATTERN.pattern() + "/(\\d{1,2})"); // 0 -> 32
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", Pattern.CASE_INSENSITIVE);


    /**
     * Generic validation based on the annotations
     * * 1. validate data
     * * 2. validate schema
     * * 3. Trim data
     *
     * @param entity entity to be validated
     * @param clazz  Class which extends Entity from which data members has to be validated
     * @return Map contains key as validation failed field and value as validation failure error
     */
    public static Future<JsonObject> validate(JsonObject entity, Class<? extends Entity> clazz, boolean update) {

        JsonObject errors = new JsonObject();

        JsonObject validated = new JsonObject();

        // validate custom entity's fields
        for (var field : clazz.getFields()) {

            try {
                validate(entity, validated, field, errors, update);
            } catch (IllegalAccessException e) {
                LOGGER.error(clazz.getSimpleName(), e);
                errors.put(clazz.getSimpleName(), "Validation failed, " + e.getMessage());
            }
        }

        return errors.isEmpty() ? Future.succeededFuture(validated) : Future.failedFuture(errors.encode());
    }

    /**
     * Validates field
     *
     * @param entity    entity to be validated
     * @param validated if validated put the updated value
     * @param field     field to be validated
     * @param errors    if error appends
     * @throws IllegalAccessException reflection failure
     */
    private static void validate(JsonObject entity, JsonObject validated, java.lang.reflect.Field field, JsonObject errors, boolean update) throws IllegalAccessException {

        if (field.isAnnotationPresent(Field.class)) {

            field.setAccessible(true);

            String key = field.get(null).toString();    // null object to get the static fields

            Field annotation = field.getAnnotation(Field.class);

            boolean isRequired = annotation.required() || annotation.unique();

            // todo: can this be optimised with code readability? do it!
            if (update) {
                if (entity.containsKey(key)) {
                    if (annotation.dataType().valid(entity, key, isRequired)) {
                        validated.put(key, entity.getValue(key));
                    } else {
                        errors.put(key, "invalid " + key); // todo: add custom meaningful error messages
                    }
                }
            } else {
                if (isRequired || entity.containsKey(key)) {
                    if (annotation.dataType().valid(entity, key, isRequired)) {
                        validated.put(key, entity.getValue(key));
                    } else {
                        errors.put(key, "invalid " + key); // todo: add custom meaningful error messages
                    }
                }
            }
        }
    }
}
