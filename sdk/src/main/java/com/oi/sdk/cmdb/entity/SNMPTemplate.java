package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.Lookup;
import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.GlobalConstants;

import java.util.List;

public class SNMPTemplate implements Entity {

    @Field(required = true)
    public static final String NAME = GlobalConstants.NAME;

    @Field
    public static final String DESCRIPTION = GlobalConstants.DESCRIPTION;

    @Field(dataType = DataType.CONTEXT)
    public static final String SCALAR_OID = "scalar_oid";

    @Field(dataType = DataType.CONTEXT)
    public static final String OBJECTS = GlobalConstants.OBJECTS;

    @Override
    public List<Lookup> references() {
        return List.of(new Lookup(ConfigCollection.SNMP_CATALOG.getName(), GlobalConstants.ID, SNMPCatalog.SNMP_TEMPLATE, "catalogs"));
    }
}
