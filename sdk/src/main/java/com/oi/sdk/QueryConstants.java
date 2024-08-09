

package com.oi.sdk;

import java.util.Arrays;

/**
 * Constants related to querying operations.
 * <p>
 * This class defines an enumeration {@code IndicatorType} for indicating the type of indicator, which can be either
 * {@code METRIC} or {@code DIMENSION}. It also provides a method {@code of} to retrieve the corresponding
 * {@code IndicatorType} from a string value.
 *

 */
public class QueryConstants {

    public enum IndicatorType {

        METRIC, DIMENSION;

        public static IndicatorType of(String value) {
            return Arrays.stream(values()).filter(e -> e.name().equalsIgnoreCase(value)).findFirst().orElseThrow(() -> new InvalidValueException("IndicatorType", value));
        }
    }
}
