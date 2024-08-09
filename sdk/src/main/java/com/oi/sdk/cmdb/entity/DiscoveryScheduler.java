package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.cmdb.validation.ToMany;
import com.oi.sdk.cmdb.validation.ToOne;
import com.oi.sdk.GlobalConstants;
import com.oi.sdk.notification.NotificationConstants;

public class DiscoveryScheduler implements Entity {

    @Field(unique = true)
    public static final String NAME = GlobalConstants.NAME;

    @Field(required = true)
    public static final String ENTITY_TYPE = GlobalConstants.ENTITY_TYPE;

    @ToMany
    @Field(required = true, dataType = DataType.LIST)
    public static final String ENTITIES = GlobalConstants.ENTITIES;

    @Field(dataType = DataType.YES_NO)
    public static final String SCHEDULER_CONTEXT_UPDATED = "scheduler_context_updated"; // to determine if scheduler context is updated? so we can update the scheduler context

    @Field(dataType = DataType.CONTEXT)
    public static final String NOTIFICATION_CONTEXT = NotificationConstants.NOTIFICATION_CONTEXT;

    @ToOne
    @Field(required = true, dataType = DataType.NUMBER)
    public static final String SCHEDULER = "scheduler"; //scheduler id > scheduler must be created before creating Discovery Profile.

    public static final String CONTEXT = "scheduler_context";
}
