package com.oi.sdk.websocket;

import com.oi.sdk.GlobalConstants;
import com.oi.sdk.engine.Engine;
import com.oi.sdk.logger.Logger;
import com.oi.sdk.Injector;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * to be implemented
 */
public class AbstractWebSocketHandler extends Engine {

    public static void reply(WebsocketAddress evenType, String sessionID, JsonObject body) {
        Injector.get(Vertx.class).eventBus().publish(WebsocketAddress.SESSION_EVENT.address(sessionID), body.put(GlobalConstants.EVENT_TYPE, evenType.address()));
    }

    public static void notification(NotificationAddress eventType, JsonObject body) {
        Injector.get(Vertx.class).eventBus().publish(WebsocketAddress.NOTIFICATION_EVENT.address(), body.put(GlobalConstants.EVENT_TYPE, eventType.address()));
    }

    /*---------------------- errors overloaded methods for developers easy of use ---------------------------*/

    public static void error(WebsocketAddress eventType, String sessionID, Throwable throwable, JsonObject body) {
        error(eventType.address(), sessionID, throwable, throwable.getMessage(), body);
    }

    public static void error(WebsocketAddress eventType, String sessionID, String error, JsonObject body) {
        error(eventType.address(), sessionID, null, error, body);
    }

    public static void error(String eventType, String sessionID, Throwable throwable, JsonObject body) {
        error(eventType, sessionID, throwable, throwable.getMessage(), body);
    }

    public static void error(String eventType, String sessionID, String error, JsonObject body) {
        error(eventType, sessionID, null, error, body);
    }

    public static void error(String eventType, String sessionID, Throwable throwable, String message, JsonObject body) {
        Injector.get(Vertx.class).eventBus().publish(WebsocketAddress.SESSION_EVENT.address(sessionID),
                body.put(GlobalConstants.EVENT_TYPE, eventType)
                        .put(GlobalConstants.STATUS, GlobalConstants.STATUS_FAIL)
                        .put(GlobalConstants.MESSAGE, message)
                        .put(GlobalConstants.ERROR, Logger.formatStackTrace(throwable)));
    }
}
