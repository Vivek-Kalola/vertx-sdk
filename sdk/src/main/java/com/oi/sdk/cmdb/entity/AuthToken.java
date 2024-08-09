package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.Field;

public class AuthToken implements Entity {

    @Field(required = true)
    public static final String ACCESS_TOKEN = "access_token";

    @Field(required = true)
    public static final String REFRESH_TOKEN = "refresh_token";

    @Field(required = true)
    public static final String SESSION_ID = "session_id";

    @Field(required = true, dataType = DataType.NUMBER)
    public static final String USER_ID = "user_id";

    @Field(required = true)
    public static final String USERNAME = User.USERNAME;
}
