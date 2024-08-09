package com.oi.sdk.cmdb;

import com.oi.sdk.GlobalConstants;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public record Lookup(String from, String localField, String foreignField, String as) {

    public static JsonObject match(List<Long> ids) {
        return JsonObject.of("$match", JsonObject.of(GlobalConstants.ID, JsonObject.of("$in", ids)));
    }

    public static JsonObject match(Long id) {
        return JsonObject.of("$match", JsonObject.of(GlobalConstants.ID, id));
    }

    public JsonArray lookup() {

        JsonArray query = new JsonArray();

        query.add(JsonObject.of("$lookup",
                JsonObject.of("from", from)
                        .put("localField", localField)
                        .put("foreignField", foreignField)
                        .put("as", as)));

        return query;
    }

    public JsonArray reference() {

        JsonArray query = JsonArray.of();

        query.add(JsonObject.of("$lookup",
                JsonObject.of("from", from)
                        .put("localField", localField)
                        .put("foreignField", foreignField)
                        .put("as", as)));

        query.add(JsonObject.of("$addFields",
                JsonObject.of(as,
                        JsonObject.of("$map",
                                JsonObject.of("input", "$" + as)
                                        .put("as", "temp")
                                        .put("in", "$$temp." + localField)))));

        return query;
    }
}
