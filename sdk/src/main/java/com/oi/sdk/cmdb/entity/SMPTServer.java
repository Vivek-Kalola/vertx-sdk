package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.cmdb.validation.Validator;
import com.oi.sdk.GlobalConstants;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;

public class SMPTServer implements Entity {

    @Field(required = true, dataType = DataType.YES_NO)
    public static final String ENABLED = "smtp_enabled";  // yes/no

    @Field(required = true)
    public static final String HOSTNAME = "smtp_hostname";

    @Field(required = true, dataType = DataType.PORT)
    public static final String PORT = "smtp_port";

    @Field(required = true, dataType = DataType.EMAIL)
    public static final String SENDER_EMAIL = "sender_email";   //email validation

    @Field(required = true)
    public static final String SECURITY = "smtp_security";  //none/ssl/tls

    @Field(required = true, dataType = DataType.YES_NO)
    public static final String AUTHENTICATION = "smtp_authentication";  // yes/no

    @Field
    public static final String USERNAME = "smtp_username";

    @Field
    public static final String PASSWORD = "smtp_password";

    public enum SecurityType {
        NONE, SSL, TLS;

        public static SecurityType of(String name) {
            return Arrays.stream(values()).parallel().filter(e -> e.name().equalsIgnoreCase(name)).findFirst().orElseThrow();
        }
    }

    @Override
    public Future<JsonObject> validate(JsonObject entity, boolean update) {
        if (GlobalConstants.YES.equalsIgnoreCase(entity.getString(ENABLED))) {
            return Validator.validate(entity, this.getClass(), false);
        }
        return Future.succeededFuture(entity.put(ENABLED, GlobalConstants.NO));
    }
}
