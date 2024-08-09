package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.GlobalConstants;

public class Dashboard implements Entity {

    @Field(unique = true)
    public static final String NAME = GlobalConstants.NAME;

    @Field
    public static final String DESCRIPTION = GlobalConstants.DESCRIPTION;

    @Field(dataType = DataType.LIST)
    public static final String CONTEXT = "dashboard_context";

    public static final String WIDGETS = "widgets";
}
