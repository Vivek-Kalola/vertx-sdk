package com.oi.sdk.engine;

import com.oi.sdk.GlobalConstants;
import io.vertx.core.AbstractVerticle;

public abstract class Engine extends AbstractVerticle implements Deployable {

    public static final String ROUTING_KEY = "routing.key";

    public String id() {
        return this.getClass().getCanonicalName();
    }

    public String clusterID() {
        return config().getString(GlobalConstants.CLUSTER_ID);
    }
}