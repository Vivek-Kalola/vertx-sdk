package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.Lookup;
import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.cmdb.validation.ToOne;
import com.oi.sdk.GlobalConstants;

import java.util.List;

public class Group implements Entity {

    @Field(unique = true)
    public static final String NAME = GlobalConstants.NAME;

    @ToOne
    @Field(required = true, dataType = DataType.NUMBER)
    public static final String PARENT_ID = "parent_id";

    public static final String CHILDREN = "children";

    public static final String DEVICES = "devices";
    public static final String POLICIES = "policies";

    @Override
    public List<Lookup> references() {
        return List.of(new Lookup(ConfigCollection.DEVICE.getName(), GlobalConstants.ID, Device.GROUPS, DEVICES),
                new Lookup(ConfigCollection.POLICY.getName(), GlobalConstants.ID, Policy.ENTITIES, POLICIES));
    }
}
