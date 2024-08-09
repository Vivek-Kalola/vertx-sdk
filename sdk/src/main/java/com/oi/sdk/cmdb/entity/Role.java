package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.Lookup;
import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.GlobalConstants;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Locale;

public class Role implements Entity {

    @Field(unique = true)
    public static final String NAME = GlobalConstants.NAME;

    @Field
    public static final String DESCRIPTION = GlobalConstants.DESCRIPTION;

    @Field(required = true, dataType = DataType.LIST)
    public static final String RBAC_CONTEXT = "rbac_context";

    public static final String DEFAULT = GlobalConstants.DEFAULT.toUpperCase(Locale.ROOT);

    @Override
    public List<Lookup> references() {
        return List.of(new Lookup(ConfigCollection.USER.getName(), GlobalConstants.ID, User.ROLE, "users"));
    }

    @Override
    public JsonObject addDefault(JsonObject entity) {

        if (entity.getJsonArray(RBAC_CONTEXT) == null) {
            entity.put(RBAC_CONTEXT, new JsonArray());
        }

        //Adding default rbac.context for authorised APIs

        if (!entity.getJsonArray(RBAC_CONTEXT).contains(GlobalConstants.READ + ":" + DEFAULT)) {
            entity.getJsonArray(RBAC_CONTEXT).add(GlobalConstants.READ + ":" + DEFAULT);
        }

        if (!entity.getJsonArray(RBAC_CONTEXT).contains(GlobalConstants.WRITE + ":" + DEFAULT)) {
            entity.getJsonArray(RBAC_CONTEXT).add(GlobalConstants.WRITE + ":" + DEFAULT);
        }

        if (!entity.getJsonArray(RBAC_CONTEXT).contains(GlobalConstants.DELETE + ":" + DEFAULT)) {
            entity.getJsonArray(RBAC_CONTEXT).add(GlobalConstants.DELETE + ":" + DEFAULT);
        }

        return entity;
    }
}
