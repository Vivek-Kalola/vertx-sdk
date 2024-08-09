package com.oi.sdk.cmdb.validation;

import com.oi.sdk.GlobalConstants;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * datatype with validation
 */
public enum DataType {

    STRING {
        @Override
        public boolean valid(JsonObject data, String key, boolean required) {

            String value = data.getString(key);

            if (value != null && (!required || !value.isEmpty())) {
                data.put(key, value.trim());
                return true;
            }

            return false;
        }
    },

    IP {
        @Override
        public boolean valid(JsonObject data, String key, boolean required) {
            return STRING.valid(data, key, required) && Validator.IP_ADDRESS_PATTERN.matcher(data.getString(key)).matches();
        }
    },

    CIDR {
        @Override
        public boolean valid(JsonObject data, String key, boolean required) {
            return STRING.valid(data, key, required) && Validator.CIDR_PATTERN.matcher(data.getString(key)).matches();
        }
    },

    PASSWORD {
        @Override
        public boolean valid(JsonObject data, String key, boolean required) {
            return STRING.valid(data, key, required) && data.getString(key).length() >= 4;
        }
    },

    EMAIL {
        @Override
        public boolean valid(JsonObject data, String key, boolean required) {
            return STRING.valid(data, key, required) && Validator.EMAIL_PATTERN.matcher(data.getString(key)).matches();
        }
    },

    OID {
        @Override
        public boolean valid(JsonObject data, String key, boolean required) {
            return STRING.valid(data, key, required);
        }
    },

    INDICATOR {
        @Override
        public boolean valid(JsonObject data, String key, boolean required) {
            return STRING.valid(data, key, required);
        }
    },

    MOBILE_NUMBER {
        @Override
        public boolean valid(JsonObject data, String key, boolean required) {
            return STRING.valid(data, key, required);
        }
    },

    YES_NO {
        @Override
        public boolean valid(JsonObject data, String key, boolean required) {
            return STRING.valid(data, key, required) && (GlobalConstants.YES.equalsIgnoreCase(data.getString(key)) || GlobalConstants.NO.equalsIgnoreCase(data.getString(key)));
        }
    },

    NUMBER {
        @Override
        public boolean valid(JsonObject data, String key, boolean required) {
            try {
                return !required || data.getNumber(key) != null;
            } catch (ClassCastException e) {
                return false;
            }
        }
    },

    PORT {
        @Override
        public boolean valid(JsonObject data, String key, boolean required) {
            return NUMBER.valid(data, key, required) && (0 < data.getNumber(key).intValue() && data.getNumber(key).intValue() <= 0xFFFF);
        }
    },

    LIST {
        @Override
        public boolean valid(JsonObject data, String key, boolean required) {

            try {

                JsonArray value = data.getJsonArray(key);

                return value != null && (!required || !value.isEmpty());

            } catch (ClassCastException e) {
                return false;
            }
        }
    },

    CONTEXT {
        @Override
        public boolean valid(JsonObject data, String key, boolean required) {

            try {

                JsonObject value = data.getJsonObject(key);

                return value != null && (!required || !value.isEmpty());

            } catch (ClassCastException e) {
                return false;
            }
        }
    };

    public boolean valid(JsonObject data, String key, boolean required) {
        return data.containsKey(key) && data.getValue(key) != null;
    }
}
