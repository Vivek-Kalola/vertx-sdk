package com.oi.sdk.cmdb.entity;

import com.oi.sdk.cmdb.validation.DataType;
import com.oi.sdk.cmdb.validation.Field;
import com.oi.sdk.GlobalConstants;

import java.util.Arrays;

public class Scheduler implements Entity {

    @Field(unique = true)
    public static final String NAME = GlobalConstants.NAME;

    @Field(required = true, dataType = DataType.NUMBER)
    public static final String START_DATE = "start_date";

    @Field(required = true)
    public static final String FREQUENCY = "frequency";

    @Field(required = true, dataType = DataType.LIST)
    public static final String SCHEDULED_TIMES = "scheduled_times";

    @Field(dataType = DataType.LIST)
    public static final String DAYS_OF_WEEK = "days_of_week";

    @Field(dataType = DataType.LIST)
    public static final String DAYS_OF_MONTH = "days_of_month";

    @Field
    public static final String CRON = "cron";

    @Field(required = true)
    public static final String SCHEDULER_TYPE = "scheduler_type";

    @Field(dataType = DataType.NUMBER)
    public static final String CONTEXT_ID = "context_id";

    @Field(required = true, dataType = DataType.YES_NO)
    public static final String ENABLE = "enable";

    public enum SchedulerFrequency {
        CUSTOM("custom"),
        DAILY("daily"),
        WEEKLY("weekly"),
        MONTHLY("monthly");

        private final String name;

        SchedulerFrequency(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static SchedulerFrequency of(String name) {
            return Arrays.stream(values()).parallel().filter(e -> e.getName().equalsIgnoreCase(name)).findFirst().orElseThrow();
        }
    }

    public enum SchedulerType {
        DISCOVERY_SCHEDULER("discovery_scheduler");

        private final String name;

        SchedulerType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static SchedulerType of(String name) {
            return Arrays.stream(values()).parallel().filter(e -> e.getName().equalsIgnoreCase(name)).findFirst().orElseThrow();
        }
    }

}
