package com.oi.sdk.engine;

import com.oi.sdk.logger.Logger;
import com.oi.sdk.logger.LoggerFactory;
import com.oi.sdk.logger.Module;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.oi.sdk.engine.Engine.ROUTING_KEY;
import static io.vertx.core.Promise.promise;

public final class LoadBalancer extends AbstractVerticle implements Deployable {

    private final Logger LOGGER;
    private final Class<? extends Engine> engine;
    private final String address;
    private final int instances;

    private final Map<String, String> routers = new HashMap<>();

    private LoadBalancer(Class<? extends Engine> engine, String address, int instances) {
        this.engine = engine;
        this.address = address;
        this.instances = instances >= 0 ? instances : 1;
        this.LOGGER = LoggerFactory.getLogger(Module.SYSTEM, engine);
    }

    public static LoadBalancer of(Class<? extends Engine> engine, String address, int instances) {
        return new LoadBalancer(engine, address, instances);
    }

    public String id() {
        return "loadbalancer:" + this.address;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        List<Future<Void>> futures = new ArrayList<>();

        for (int key = 1; key <= this.instances; key++) {

            Promise<Void> promise = promise();

            futures.add(promise.future());

            String routingKey = routingKey(key, address);

            vertx.deployVerticle(this.engine.getCanonicalName(), new DeploymentOptions().setConfig(JsonObject.of(ROUTING_KEY, routingKey)))
                    .onSuccess(s -> {
                        routers.put(routingKey, s);
                        LOGGER.info(routingKey + " deployed!");
                        promise.complete();
                    })
                    .onFailure(promise::fail);
        }

        Future.join(futures)
                .onSuccess(s -> {

                    // load balancing algorithm (current algorithm is round-robin)
                    AtomicInteger robin = new AtomicInteger(1);

                    vertx.eventBus().<JsonObject>localConsumer(address, message -> {

                        vertx.eventBus().send(routingKey(robin.getAndIncrement(), address), message.body());

                        if (robin.get() > instances) {
                            robin.set(1);
                        }
                    });

                    startPromise.complete();
                })
                .onFailure(startPromise::fail);
    }

    private String routingKey(int key, String address) {
        return String.format("%d-%s", key, address);
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {

        try {

            List<Future<Void>> futures = new ArrayList<>();

            for (var engine : routers.values()) {

                Promise<Void> promise = promise();

                futures.add(promise.future());

                vertx.undeploy(engine).onComplete(e -> promise.complete());
            }

            Future.join(futures).onComplete(e -> stopPromise.complete());

        } catch (Exception e) {
            stopPromise.tryFail(e);
        }
    }
}
