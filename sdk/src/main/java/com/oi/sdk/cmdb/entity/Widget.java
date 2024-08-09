package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.GlobalConstants;
import com.oi.sdk.InvalidValueException;
import com.oi.sdk.ReportDBConstants;

import java.util.Arrays;

public class Widget implements Entity {

    @Field(unique = true)
    public static final String NAME = GlobalConstants.NAME;

    @Field
    public static final String DESCRIPTION = GlobalConstants.DESCRIPTION;

    @Field(required = true)
    public static final String WIDGET_TYPE = "widget_type";

    @Field(required = true)
    public static final String DATASOURCE = ReportDBConstants.DATASOURCE;

    @Field(required = true)
    public static final String INDICATOR_GROUP = "indicator_group";

    @Field(required = true)
    public static final String TIME_RANGE = "time_range";

    @Field
    public static final String GRANULARITY = "granularity";

    @Field(required = true, dataType = DataType.LIST)
    public static final String INDICATORS = "indicators";

    @Field
    public static final String OBJECT_TYPE = GlobalConstants.OBJECT_TYPE;

    @Field(required = true)
    public static final String GROUP_BY = "group_by";

    @Field(dataType = DataType.CONTEXT)
    public static final String ORDER_BY = "order_by";

    @Field(dataType = DataType.NUMBER)
    public static final String LIMIT = "limit";

    @Field(dataType = DataType.CONTEXT)
    public static final String FILTERS = "filters";

    /**
     * widget types
     */
    public enum WidgetType {
        CHART("chart"),
        TOP_N("topN"),
        GRID("grid"),
        GAUGE("gauge"),
        ;

        private final String name;

        WidgetType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static WidgetType of(String value) {
            return Arrays.stream(values()).filter(e -> e.getName().equalsIgnoreCase(value)).findFirst().orElseThrow(() -> new InvalidValueException("WidgetType", value));
        }
    }

    public enum IndicatorGroup {

        AVAILABILITY("availability"),
        METRIC("metric"),
        FLOW("flow"),
        LOG("log"),
        ALERT("alert"),
        AUDIT("audit"),
        ;

        private final String name;

        IndicatorGroup(String type) {
            this.name = type;
        }

        public String getType() {
            return name;
        }

        public static IndicatorGroup of(String value) {
            return Arrays.stream(values()).filter(e -> e.name.equalsIgnoreCase(value)).findFirst().orElseThrow(() -> new InvalidValueException(INDICATOR_GROUP, value));
        }

    }

}
