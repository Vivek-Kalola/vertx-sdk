package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.Lookup;
import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.GlobalConstants;

import java.util.List;

public class Site implements Entity {

    @Field(unique = true)
    public static final String NAME = GlobalConstants.NAME;

    @Field
    public static final String DESCRIPTION = GlobalConstants.DESCRIPTION;

    @Field
    public static final String SITE_CODE = "site_code";

    @Field
    public static final String LOCATION = "location";

    @Field
    public static final String COUNTRY = "country";

    @Field(dataType = DataType.NUMBER)
    public static final String LATITUDE = "latitude";

    @Field(dataType = DataType.NUMBER)
    public static final String LONGITUDE = "longitude";

    @Field(required = true, dataType = DataType.NUMBER)
    public static final String PARENT_ID = "parent_id";

    @Override
    public List<Lookup> references() {
        return List.of(new Lookup(ConfigCollection.DEVICE.getName(), GlobalConstants.ID, Device.GROUPS, "devices"));
    }
}
