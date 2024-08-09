package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.cmdb.validation.ToOne;
import com.oi.sdk.GlobalConstants;

public class SNMPCatalog implements Entity {

    @Field(unique = true)
    public static final String NAME = GlobalConstants.NAME;

    @Field
    public static final String DESCRIPTION = GlobalConstants.DESCRIPTION;

    @Field
    public static final String VENDOR = "vendor";

    @Field
    public static final String MODEL = "model";

    @Field
    public static final String OS = "os";

    @Field(required = true, dataType = DataType.OID)
    public static final String SYSTEM_OID = "system_oid";

    @ToOne
    @Field(required = true, dataType = DataType.NUMBER)
    public static final String SNMP_TEMPLATE = "snmp_template";   // foreign key: snmp-template
}
