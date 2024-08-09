package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.cmdb.validation.ToMany;
import com.oi.sdk.GlobalConstants;
import com.oi.sdk.notification.NotificationConstants;

public class Policy implements Entity {

    @Field(unique = true)
    public static final String NAME = GlobalConstants.NAME;

    @Field
    public static final String DESCRIPTION = GlobalConstants.DESCRIPTION;

    @Field(dataType = DataType.LIST)
    public static final String TAGS = "tags";

    @Field(required = true)
    public static final String ENTITY_TYPE = GlobalConstants.ENTITY_TYPE;

    @ToMany
    @Field(required = true, dataType = DataType.LIST)
    public static final String ENTITIES = GlobalConstants.ENTITIES;

    @Field(required = true, dataType = DataType.CONTEXT)
    public static final String THRESHOLD = "threshold";

    @Field(required = true)
    public static final String OBJECT_TYPE = GlobalConstants.OBJECT_TYPE;

    @Field(required = true, dataType = DataType.INDICATOR)
    public static final String INDICATOR = "indicator";

    @Field(required = true)
    public static final String OPERATOR = "operator";

    @Field(required = true, dataType = DataType.NUMBER)
    public static final String OCCURRENCES = "occurrences";

    @Field(required = true, dataType = DataType.NUMBER)
    public static final String TIMEFRAME = "timeframe";

    @Field(required = true)
    public static final String TIMEFRAME_UNIT = "timeframe_unit";

    @Field(required = true, dataType = DataType.NUMBER)
    public static final String AUTO_CLEAR_TIME = "auto_clear";

    @Field(required = true)
    public static final String AUTO_CLEAR_TIME_UNIT = "time_frame_unit";

    @Field(dataType = DataType.CONTEXT)
    public static final String NOTIFICATION_CONTEXT = NotificationConstants.NOTIFICATION_CONTEXT;
}
