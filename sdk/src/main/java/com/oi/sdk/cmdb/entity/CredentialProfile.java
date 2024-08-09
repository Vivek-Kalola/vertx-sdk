package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.Lookup;
import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.GlobalConstants;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;
import java.util.List;

public class CredentialProfile implements Entity {

    public enum Protocol {
        SSH, SNMP_V1, SNMP_V2C, SNMP_V3;

        public static Protocol of(String value) {
            return Arrays.stream(values()).filter(e -> e.toString().equals(value)).findFirst().orElse(null);
        }
    }

    @Field(unique = true)
    public static final String NAME = GlobalConstants.NAME;

    @Field(required = true)
    public static final String PROTOCOL = "protocol";

    @Field(required = true, dataType = DataType.CONTEXT)
    public static final String CONTEXT = "credential_context";

    // SNMP_V1 & SNMP_V2C
    public static final String SNMP_COMMUNITY = "snmp_community";

    // SNMP_V3
    public static final String SNMP_SECURITY = "snmp_security";
    public static final String AUTH_PROTOCOL = "auth_protocol";
    public static final String AUTH_PASSWORD = "auth_password";
    public static final String PRIVACY_PROTOCOL = "privacy_protocol";
    public static final String PRIVACY_PASSWORD = "privacy_password";

    @Override
    public List<Lookup> references() {
        return List.of(new Lookup(ConfigCollection.DEVICE.getName(), GlobalConstants.ID, Device.CREDENTIAL_PROFILES, "devices"));
    }

    @Override
    public Future<JsonObject> validate(JsonObject entity, boolean update) {

        return Entity.super.validate(entity, update)
                .compose(profile -> {
                    if (Protocol.of(profile.getString(PROTOCOL)) == null) {
                        return Future.failedFuture("invalid " + PROTOCOL);
                    } else {
                        return Future.succeededFuture(profile);
                    }
                });
    }
}
