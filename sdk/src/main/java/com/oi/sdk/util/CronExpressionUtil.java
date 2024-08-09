package com.oi.sdk.util;

import com.google.common.base.Joiner;
import com.oi.sdk.logger.Logger;
import com.oi.sdk.logger.LoggerFactory;
import com.oi.sdk.logger.Module;
import com.oi.sdk.cmdb.entity.Scheduler;
import com.oi.sdk.cmdb.entity.Scheduler.SchedulerFrequency;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.quartz.DateBuilder;

import java.util.ArrayList;
import java.util.List;

public class CronExpressionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(Module.UTILS, CronExpressionUtil.class);

    private CronExpressionUtil() {
    }

    /**
     * Creates cron string based on the given context
     */
    public static List<String> getCronExpressions(JsonObject context) {

        List<String> crons = new ArrayList<>();

        try {

            if (SchedulerFrequency.CUSTOM.name().equalsIgnoreCase(context.getString(Scheduler.FREQUENCY))) {  // if it's custom cron just add and return
                crons.add(context.getString(Scheduler.CRON));
                return crons;
            }

            for (var time : context.getJsonArray(Scheduler.SCHEDULED_TIMES)) {

                String[] tokens = time.toString().split(":");

                Integer hours = Integer.parseInt(tokens[0]);
                DateBuilder.validateHour(hours);

                Integer minutes = Integer.parseInt(tokens[1]);
                DateBuilder.validateMinute(minutes);

                SchedulerFrequency frequency = SchedulerFrequency.of(context.getString(Scheduler.FREQUENCY));

                if (frequency != null) {

                    switch (frequency) {

                        case DAILY -> {

                            crons.add(String.format("0 %d %d ? * *", minutes, hours));
                            // Ref: CronScheduleBuilder.dailyAtHourAndMinute();
                        }

                        case WEEKLY -> {

                            JsonArray daysOfWeek = context.getJsonArray(Scheduler.DAYS_OF_WEEK);

                            if (daysOfWeek != null) {

                                for (int i = 0; i < daysOfWeek.size(); i++) {
                                    DateBuilder.validateDayOfWeek(daysOfWeek.getInteger(i));
                                }

                                String days = Joiner.on(",").join(daysOfWeek.stream().toList());

                                crons.add(String.format("0 %d %d ? * %s", minutes, hours, days));
                                //Ref: CronScheduleBuilder.atHourAndMinuteOnGivenDaysOfWeek();

                            } else {
                                LOGGER.warn(String.format("Invalid CRON: %s is missing in %s", Scheduler.DAYS_OF_WEEK, context.encode()));
                            }
                        }

                        case MONTHLY -> {

                            JsonArray daysOfMonth = context.getJsonArray(Scheduler.DAYS_OF_MONTH);

                            if (daysOfMonth != null) {

                                for (int i = 0; i < daysOfMonth.size(); i++) {
                                    DateBuilder.validateDayOfMonth(daysOfMonth.getInteger(i));
                                }

                                String days = Joiner.on(",").join(daysOfMonth.stream().toList());
                                crons.add(String.format("0 %d %d %s * ?", minutes, hours, days));
                                // Ref: CronScheduleBuilder.monthlyOnDayAndHourAndMinute()

                            } else {
                                LOGGER.warn(String.format("Invalid CRON: %s is missing in %s", Scheduler.DAYS_OF_MONTH, context.encode()));
                            }
                        }
                    }

                } else {
                    LOGGER.warn("Frequency is null in : " + context.encode());
                }
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }

        return crons;
    }
}
