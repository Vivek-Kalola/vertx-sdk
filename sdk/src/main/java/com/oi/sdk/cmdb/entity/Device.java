package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.Lookup;
import com.oi.sdk.GlobalConstants;
import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.FailedToDeleteException;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.cmdb.validation.ToMany;
import com.oi.sdk.cmdb.validation.ToOne;
import io.vertx.core.json.JsonObject;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

public class Device implements Entity {

    @Field(required = true)
    public static final String PLUGIN_TYPE = "plugin_type";

    @Field(required = true)
    public static final String PROFILE_TYPE = "profile_type";

    @Field(required = true, dataType = DataType.IP)
    public static final String IP_ADDRESS = "ip_address";

    @Field(unique = true)
    public static final String HOSTNAME = "hostname";

    @Field(required = true, dataType = DataType.PORT)
    public static final String PORT = "port";

    @ToMany
    @Field(required = true, dataType = DataType.LIST)
    public static final String CREDENTIAL_PROFILES = "credential_profiles";     //foreign foreignKey

    @ToMany
    @Field(required = true, dataType = DataType.LIST)
    public static final String GROUPS = "groups";       //foreign foreignKey

    @ToOne
    @Field(required = true, dataType = DataType.NUMBER)
    public static final String SITE = "site";            //foreign foreignKey

    @Field(required = true, dataType = DataType.YES_NO)
    public static final String AUTO_PROVISION = "auto_provision";

    @Field(required = true, dataType = DataType.YES_NO)         // default field NO
    public static final String SKIP_PING_CHECK = "skip_ping_check";

    @Field(required = true, dataType = DataType.YES_NO)     // default field NO
    public static final String FLOW_ENABLED = "flow_enabled";

    @Field(required = true)                         // default field NEW
    public static final String STATUS = "status";

    @Field
    public static final String DEVICE_TYPE = "device_type";

    @Field
    public static final String ALIAS = "alias";

    @Field
    public static final String VENDOR = "vendor";

    @Field
    public static final String OEM = "oem";

    @Field
    public static final String OS = "os";

    @Field
    public static final String OS_VERSION = "os_version";

    @Field
    public static final String TIMEZONE = "timezone";

    @Field(dataType = DataType.NUMBER)
    public static final String VALID_CREDENTIAL_PROFILE = "valid_credential_profile";

    public static final String UPDATE_CREDENTIAL_PROFILE = "update_credential_profile";

    @ToOne
    @Field(dataType = DataType.NUMBER)
    public static final String SNMP_TEMPLATE = "snmp_template";

    @Field(dataType = DataType.NUMBER)
    public static final String DISCOVERED_ON = "discovered_on";

    @Field(dataType = DataType.NUMBER)
    public static final String AVAILABILITY_ON = "availability_on";

    public static final String OBJECTS = "objects";

    @Override
    public List<Lookup> references() {
        return List.of(new Lookup(ConfigCollection.POLICY.getName(), GlobalConstants.ID, Policy.ENTITIES, "polices"));
    }

    @Override
    public List<Lookup> lookups() {
        return List.of(new Lookup(ConfigCollection.GROUP.getName(), GROUPS, GlobalConstants.ID, GROUPS + "_lookup"));
    }

    public enum DeviceStatus {

        NEW("new") {
            @Override
            public DeviceStatus getNextStatus(boolean success) {
                return success ? DISCOVERED : DISCOVERY_FAILED;
            }
        },
        DISCOVERED("discovered") {
            @Override
            public DeviceStatus getNextStatus(boolean success) {
                return success ? DISCOVERED : DISCOVERY_FAILED;
            }
        },
        MONITORING("monitoring") {
            @Override
            public DeviceStatus getNextStatus(boolean success) {
                return MONITORING;
            }
        },
        DISCOVERY_FAILED("discovery_failed") {
            @Override
            public DeviceStatus getNextStatus(boolean success) {
                return success ? DISCOVERED : DISCOVERY_FAILED;
            }
        },
        DISABLED("disabled") {
            @Override
            public DeviceStatus getNextStatus(boolean success) {
                throw new IllegalCallerException("disabled devices cannot be discovered");
            }
        };

        private final String name;

        DeviceStatus(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public abstract DeviceStatus getNextStatus(boolean success);

        public static DeviceStatus of(String name) {
            return Arrays.stream(values()).parallel().filter(e -> e.getName().equalsIgnoreCase(name)).findFirst().orElseThrow();
        }
    }

    public enum PluginType {
        SNMP,
        SSH,
        API;

        public String getName() {
            return name();
        }

        public static PluginType of(String value) {
            return Arrays.stream(values())
                    .parallel()
                    .filter(type -> type.name().equalsIgnoreCase(value))
                    .findFirst()
                    .orElseThrow();
        }
    }

    @Override
    public JsonObject addDefault(JsonObject entity) {

        putIfAbsent(entity, STATUS, DeviceStatus.NEW.getName());
        putIfAbsent(entity, AUTO_PROVISION, GlobalConstants.YES);
        putIfAbsent(entity, TIMEZONE, ZoneId.systemDefault().getId());
        putIfAbsent(entity, FLOW_ENABLED, GlobalConstants.NO);
        putIfAbsent(entity, PROFILE_TYPE, DeviceManager.ProfileType.IP.getName());
        putIfAbsent(entity, HOSTNAME, entity.getString(Device.IP_ADDRESS)); // hostname is same as IP Address
        putIfAbsent(entity, SITE, GlobalConstants.DEFAULT_ID);
        putIfAbsent(entity, SKIP_PING_CHECK, GlobalConstants.NO);

        return entity;
    }

    @Override
    public void validateDeleteViaAPI(JsonObject entity) throws FailedToDeleteException {

        Entity.super.validateDeleteViaAPI(entity);

        if (DeviceStatus.MONITORING.getName().equalsIgnoreCase(entity.getString(STATUS))) {
            throw new FailedToDeleteException("Device is in " + DeviceStatus.MONITORING.getName() + " state");
        }

        if (GlobalConstants.YES.equalsIgnoreCase(entity.getString(FLOW_ENABLED))) {
            throw new FailedToDeleteException("Flow is enabled");
        }
    }
}
