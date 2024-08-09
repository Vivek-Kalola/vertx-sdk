package com.oi.sdk.audit;

import com.oi.sdk.logger.Logger;
import com.oi.sdk.logger.LoggerFactory;
import com.oi.sdk.logger.Module;
import com.oi.sdk.GlobalConstants;
import com.oi.sdk.eventbus.EventBusAddress;
import com.oi.sdk.util.DatetimeUtil;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public final class Audit {

    private static final Logger LOGGER = LoggerFactory.getLogger(Module.AUDIT, Audit.class);

    private final long timestamp;
    private final String username;
    private final String remoteHost;
    private String collection;
    private AuditConstants.Action action;
    private boolean success = true;
    private JsonObject entity;
    private Long id;
    private List<Long> ids;

    private Audit(String username, String remoteHost) {
        this.username = username;
        this.remoteHost = remoteHost;
        this.timestamp = DatetimeUtil.now();
    }

    public String username() {
        return username;
    }

    public String remoteHost() {
        return remoteHost;
    }

    public static Audit system() {
        return Audit.system("0.0.0.0");
    }

    public static Audit system(String remoteHost) {
        return new Audit(GlobalConstants.SYSTEM_USER, remoteHost);
    }

    public static Audit of(RoutingContext routingContext) {

        User user = routingContext.user();

        if (user != null && user.principal().containsKey(AuditConstants.USERNAME)) {
            return new Audit(user.principal().getString(AuditConstants.USERNAME), routingContext.request().remoteAddress().host());
        } else {
            return system(routingContext.request().remoteAddress().host());
        }
    }

    public Audit success() {
        this.success = true;
        return this;
    }

    public Audit failed() {
        this.success = false;
        return this;
    }

    public Audit create(String collection, JsonObject entity) {
        this.collection = collection;
        this.action = AuditConstants.Action.CREATE;
        this.entity = entity.copy();
        return this;
    }

    public Audit update(String collection, Long id, JsonObject entity) {
        this.collection = collection;
        this.action = AuditConstants.Action.UPDATE;
        this.entity = entity.copy();
        this.id = id;
        return this;
    }

    public Audit delete(String collection, Long id) {
        this.collection = collection;
        this.action = AuditConstants.Action.DELETE;
        this.id = id;
        return this;
    }

    public Audit delete(String collection, List<Long> ids) {
        this.collection = collection;
        this.action = AuditConstants.Action.DELETE;
        this.ids = ids;
        return this;
    }

    /**
     * audit object creation logic is written here
     * todo: #simplificationRequired
     */
    public void publish(Vertx vertx) {

        try {

            JsonObject audit = new JsonObject();

            audit.put(GlobalConstants.TIMESTAMP, timestamp)
                    .put(AuditConstants.COLLECTION, collection)
                    .put(AuditConstants.REMOTE_HOST, remoteHost)
                    .put(AuditConstants.ACTION, action.name())
                    .put(AuditConstants.USERNAME, username)
                    .put(AuditConstants.STATUS, success ? GlobalConstants.STATUS_SUCCESS : GlobalConstants.STATUS_FAIL)
                    .put(AuditConstants.MESSAGE, success ? action.success(collection) : action.fail(collection));

            switch (action) {
                case CREATE -> audit.put(AuditConstants.ENTITY, entity);
                case UPDATE -> audit.put(AuditConstants.ENTITY, entity).put(GlobalConstants.ID, id);
                case DELETE -> audit.put(GlobalConstants.ID, id);
            }

            if (ids != null && !ids.isEmpty()) {

                for (Long id : ids) {
                    vertx.eventBus().send(EventBusAddress.REPORT_DB_AUDIT_WRITER.address(), audit.copy().put(GlobalConstants.ID, id));
                }

            } else {
                vertx.eventBus().send(EventBusAddress.REPORT_DB_AUDIT_WRITER.address(), audit);
            }

        } catch (Exception e) {
            LOGGER.error(e);
        }
    }
}