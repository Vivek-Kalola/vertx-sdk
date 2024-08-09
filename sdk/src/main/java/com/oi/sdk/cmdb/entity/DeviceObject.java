package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.cmdb.validation.ToOne;
import com.oi.sdk.GlobalConstants;

public class DeviceObject implements Entity {

    @Field(required = true)
    public static final String OBJECT_TYPE = GlobalConstants.OBJECT_TYPE;

    @ToOne
    @Field(required = true, dataType = DataType.NUMBER)
    public static final String DEVICE_ID = GlobalConstants.DEVICE_ID;

    @Field(required = true, dataType = DataType.YES_NO)
    public static final String ENABLED = GlobalConstants.ENABLED;

    @Field(required = true, dataType = DataType.NUMBER)
    public static final String DEFAULT_POLLING_INTERVAL = "default_polling_interval";

    @Field(required = true, dataType = DataType.NUMBER)
    public static final String POLLING_INTERVAL = "polling_interval";

    public static final String AVAILABILITY = "availability";
}
