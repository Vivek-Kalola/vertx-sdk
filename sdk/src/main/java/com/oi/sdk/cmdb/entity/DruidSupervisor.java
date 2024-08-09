
package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.GlobalConstants;

/**
 * Represents a Druid Supervisor entity.
 * <p>
 * This class implements the Entity interface.
 *

 */
public class DruidSupervisor implements Entity {

    @Field(unique = true)
    public static final String NAME = GlobalConstants.NAME;

    @Field(required = true, dataType = DataType.CONTEXT)
    public static final String SUPERVISOR_SPEC = "supervisor_spec";
}
