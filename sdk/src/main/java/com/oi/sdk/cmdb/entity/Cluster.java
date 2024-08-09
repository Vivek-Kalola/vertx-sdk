package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.GlobalConstants;

import java.util.Arrays;

public class Cluster implements Entity {

    @Field(required = true)
    public static final String NAME = GlobalConstants.NAME;

    @Field(unique = true)
    public static final String CLUSTER_ID = GlobalConstants.CLUSTER_ID;

    @Field(required = true, dataType = DataType.IP)
    public static final String IP_ADDRESS = "ip_address";

    @Field(required = true)
    public static final String CLUSTER_TYPE = "cluster_type";

    @Field(required = true, dataType = DataType.NUMBER)
    public static final String DISCOVERED_ON = "discovered_on";

    @Field(required = true, dataType = DataType.NUMBER)
    public static final String REDISCOVERED_ON = "rediscovered_on";

    public enum MemberType {
        KERNEL,
        GATEWAY,
        COLLECTOR,
        ALERT_ENGINE,
        NOTIFICATION_ENGINE,
        VISUALIZATION,
        DRUID_ADAPTOR,
        ;

        public static MemberType of(String value) {
            return Arrays.stream(values())
                    .parallel()
                    .filter(type -> type.name().equalsIgnoreCase(value))
                    .findFirst()
                    .orElseThrow();
        }
    }
}
