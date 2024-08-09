
package com.oi.sdk.util;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.codec.impl.BodyCodecImpl;

public class DecoderUtils {

    public static JsonArray decodeJsonArray(Buffer buffer) {
        return BodyCodecImpl.JSON_ARRAY_DECODER.apply(buffer);
    }
}
