package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.SMPTServer;
import com.oi.sdk.GlobalConstants;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class SMTPServerStore extends AbstractStore {

    private SMTPServerStore() {
    }

    private static SMTPServerStore INSTANCE;

    public static SMTPServerStore get() {
        if (INSTANCE == null) {
            INSTANCE = new SMTPServerStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.SMTP_SERVER.getName();
    }

    @Override
    public SMPTServer entity() {
        return new SMPTServer();
    }

    public Future<JsonObject> configuration() {
        return this.find(GlobalConstants.DEFAULT_ID);
    }
}
