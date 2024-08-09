package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.validation.*;
import com.oi.sdk.GlobalConstants;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.time.ZoneId;
import java.util.Arrays;

public class DeviceManager implements Entity {

    @Field(unique = true)
    public static final String PROFILE_NAME = "profile_name";

    @Field(required = true)
    public static final String PROFILE_TYPE = Device.PROFILE_TYPE;

    @Field(dataType = DataType.CIDR)
    public static final String CIDR = "cidr";

    @Field(dataType = DataType.IP)
    public static final String START_IP = "start_ip";

    @Field(dataType = DataType.IP)
    public static final String END_IP = "end_ip";

    @Field(required = true, dataType = DataType.PORT)
    public static final String PORT = Device.PORT;

    @Field(required = true)
    public static final String PLUGIN_TYPE = Device.PLUGIN_TYPE;

    @ToMany
    @Field(required = true, dataType = DataType.LIST)
    public static final String CREDENTIAL_PROFILES = Device.CREDENTIAL_PROFILES;     //foreign foreignKey

    @ToMany
    @Field(required = true, dataType = DataType.LIST)
    public static final String GROUPS = Device.GROUPS;       //foreign foreignKey

    @ToOne
    @Field(required = true, dataType = DataType.NUMBER)
    public static final String SITE = Device.SITE;            //foreign foreignKey

    @Field(required = true, dataType = DataType.YES_NO)
    public static final String AUTO_PROVISION = Device.AUTO_PROVISION;

    @Field(required = true, dataType = DataType.YES_NO)
    public static final String SKIP_PING_CHECK = Device.SKIP_PING_CHECK;

    @Field(required = true, dataType = DataType.YES_NO)
    public static final String FLOW_ENABLED = Device.FLOW_ENABLED;

    @Field
    public static final String DEVICE_TYPE = Device.DEVICE_TYPE;
    @Field
    public static final String ALIAS = Device.ALIAS;
    @Field
    public static final String VENDOR = Device.VENDOR;
    @Field
    public static final String OEM = Device.OEM;
    @Field
    public static final String OS = SNMPCatalog.OS;
    @Field
    public static final String OS_VERSION = Device.OS_VERSION;
    @Field
    public static final String TIMEZONE = Device.TIMEZONE;

    @Field(dataType = DataType.CONTEXT)
    public static final String DEVICE_LIST = "device_list";


    public enum ProfileType {

        IP("ip_address"),       // Device
        CIDR("cidr"),           // Profile
        IP_RANGE("ip_range"),   // Profile
        CSV("csv");             // Profile

        private final String name;

        ProfileType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static ProfileType of(String name) {
            return Arrays.stream(values()).filter(e -> name.equalsIgnoreCase(e.getName())).findFirst().orElseThrow();
        }
    }

    @Override
    public JsonObject addDefault(JsonObject entity) {

        putIfAbsent(entity, AUTO_PROVISION, GlobalConstants.YES);
        putIfAbsent(entity, TIMEZONE, ZoneId.systemDefault().getId());
        putIfAbsent(entity, FLOW_ENABLED, GlobalConstants.NO);
        putIfAbsent(entity, SKIP_PING_CHECK, GlobalConstants.NO);
        putIfAbsent(entity, SITE, GlobalConstants.DEFAULT_ID);

        return entity;
    }

    @Override
    public Future<JsonObject> validate(JsonObject entity, boolean update) {

        // todo: custom validation for each profile to be added here!

        if (entity.getString(PROFILE_TYPE).equalsIgnoreCase(ProfileType.CSV.getName())) {
            return Validator.validate(entity, this.getClass(), true); //Although upsert is not an update operation but in case of CSV, fields should be validated only if those are present
        } else {
            return Validator.validate(entity, this.getClass(), update);
        }
    }
}
