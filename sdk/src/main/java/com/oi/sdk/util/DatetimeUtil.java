

package com.oi.sdk.util;

import java.util.Arrays;

/**
 * @author Vvek
 */
public class DatetimeUtil {

    public enum TimeUnit {
        SECOND(1),
        MINUTE(60),
        HOUR(3600);

        private final int multiplier;

        TimeUnit(int multiplier) {
            this.multiplier = multiplier;
        }

        public static TimeUnit of(String value) {
            return Arrays.stream(values()).filter(e -> e.toString().equalsIgnoreCase(value)).findFirst().orElse(null);
        }

        public long toSeconds(long value) {
            return value * multiplier;
        }
    }

    /**
     * @return current time in seconds
     */
    public static long now() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * @return current time in millis
     */
    public static long millis() {
        return System.currentTimeMillis();
    }

    /**
     * @return current time in nano
     */
    public static long nano() {
        return System.nanoTime();
    }
}
