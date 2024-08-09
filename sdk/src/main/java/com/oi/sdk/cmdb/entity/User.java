package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.GlobalConstants;
import io.vertx.core.json.JsonObject;

public class User implements Entity {

    @Field(unique = true)
    public static final String USERNAME = "username";

    @Field(required = true, dataType = DataType.PASSWORD)
    public static final String PASSWORD = "password";

    @Field
    public static final String FIRST_NAME = "first_name";

    @Field
    public static final String LAST_NAME = "last_name";

    @Field(required = true, dataType = DataType.EMAIL)
    public static final String EMAIL_ADDRESS = "email_address";

    @Field(dataType = DataType.MOBILE_NUMBER)
    public static final String MOBILE_NUMBER = "mobile_number";

    @Field(required = true, dataType = DataType.NUMBER)
    public static final String ROLE = "role";

    @Field(required = true, dataType = DataType.LIST)
    public static final String GROUPS = "groups";

    @Field(required = true, dataType = DataType.YES_NO)
    public static final String ENABLE = "enable";

    @Field(dataType = DataType.LIST)
    public static final String TENANTS = "tenants";

    @Field(required = true, dataType = DataType.CONTEXT)
    public static final String USER_PREFERENCE = "user_preference";

    public static final String THEME = "theme";

    public static final String DEFAULT_DASHBOARD = "default_dashboard";

    public enum Theme {
        LIGHT,
        DARK,
        SYSTEM
    }

    @Override
    public JsonObject addDefault(JsonObject entity) {

        putIfAbsent(entity, ENABLE, GlobalConstants.YES);
        putIfAbsent(entity, USER_PREFERENCE, JsonObject.of(THEME, Theme.SYSTEM.name(), DEFAULT_DASHBOARD, 1000000000001L));

        return entity;
    }
}
