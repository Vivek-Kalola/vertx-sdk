package com.oi.sdk;

import com.google.common.base.Strings;
import com.oi.sdk.cmdb.IConfigRepository;
import com.oi.sdk.cmdb.MongoDBRepository;
import com.oi.sdk.cmdb.entity.Cluster;
import com.oi.sdk.engine.Deployable;
import com.oi.sdk.eventbus.ClusterAddress;
import com.oi.sdk.logger.Logger;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.SLF4JLogDelegateFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static io.vertx.core.Promise.promise;

public abstract class AppRunner {

    private static final Map<String, String> DEPLOYED_ENGINES = new ConcurrentHashMap<>();

    public static final int HEARTBEAT_INTERVAL_MS = 2000;

    protected static final String CLUSTER_NAME = "oi-vertxio";

    private final Vertx vertx;
    private final Logger LOGGER;
    private String clusterID;

    public AppRunner(Vertx vertx, Logger logger, SystemConfig configService) {
        LOGGER = logger;

        String logFactory = System.getProperty("org.vertx.logger-delegate-factory-class-name");

        if (logFactory == null) {
            System.setProperty("org.vertx.logger-delegate-factory-class-name", SLF4JLogDelegateFactory.class.getName());
        }

        this.vertx = vertx;

        Injector.inject(Vertx.class, vertx);             // register vertx instance (not recommended to abuse this, use only if better architecture is not available)
        Injector.inject(configService);         // register for Mirco-service
        Injector.inject(SystemConfig.class, configService);     // register duplicate for SDK use
        Injector.inject(IConfigRepository.class, new MongoDBRepository());      // register repository

//        LogManager.getLogManager().reset();
    }

    public Vertx vertx() {
        return vertx;
    }

    protected abstract Cluster.MemberType memberType();

    /**
     * Each microservice should have to initialize the AppRunner once Vertx object is created
     *
     * @return future object
     */
    public final Future<Boolean> initialize() {

//        vertx.eventBus().<String>localConsumer(EventBusAddress.BOOT_ENGINE.address(), message -> boot(message.body()).onSuccess(message::reply).onFailure(e -> message.fail(-1, e.getMessage())));
//        vertx.eventBus().<String>localConsumer(EventBusAddress.REBOOT_ENGINE.address(), message -> reboot(message.body()).onSuccess(message::reply).onFailure(e -> message.fail(-1, e.getMessage())));
//        vertx.eventBus().<String>localConsumer(EventBusAddress.SHUTDOWN_ENGINE.address(), message -> shutdown(message.body()).onSuccess(message::reply).onFailure(e -> message.fail(-1, e.getMessage())));

        Promise<Void> promise = promise();

        ConfigRetriever retriever = ConfigRetriever.create(vertx(), new ConfigRetrieverOptions().addStore(new ConfigStoreOptions().setType("file")
                .setConfig(new JsonObject().put("path", GlobalConstants.CONFIG_DIR + GlobalConstants.PATH_SEPARATOR + GlobalConstants.SYSTEM_CONFIG_FILE))));

        retriever.listen(change -> Injector.get(SystemConfig.class).onChange(change.getNewConfiguration()));

        retriever.getConfig()
                .onSuccess(config -> {

                    SystemConfig configuration = Injector.get(SystemConfig.class);

                    configuration.init(config);

                    String metafile = GlobalConstants.METADATA_DIR + GlobalConstants.PATH_SEPARATOR + configuration.getModuleID() + ".meta";

                    if (vertx.fileSystem().existsBlocking(metafile)) {

                        String id = vertx.fileSystem().readFileBlocking(metafile).toString();

                        if (!Strings.isNullOrEmpty(id)) {
                            clusterID = id;
                        }
                    }

                    if (Strings.isNullOrEmpty(clusterID)) {

                        clusterID = UUID.randomUUID().toString();

                        try {

                            Path metadata = Path.of(GlobalConstants.METADATA_DIR);

                            if (!Files.exists(metadata)) {
                                Files.createDirectory(metadata);
                            }

                            Path _metafile = Path.of(metafile);

                            Files.deleteIfExists(_metafile);

                            Files.writeString(_metafile, clusterID, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);

                        } catch (IOException e) {
                            exit(e);
                        }
                    }

                    Injector.get(IConfigRepository.class).init(vertx)
                            .onSuccess(promise::complete)
                            .onFailure(promise::fail);
                })
                .onFailure(promise::fail);

        if (memberType() == Cluster.MemberType.KERNEL) {
            return promise.future()
                    .compose(v -> Injector.get(IConfigRepository.class).initCollections());
        }

        return promise.future()
                .compose(v -> register());
    }


    /**
     * Method to register the microservice with kernel
     */
    protected Future<Boolean> register() {

        Promise<Boolean> promise = promise();

        try {

            vertx.executeBlocking(() -> {
                try (final DatagramSocket socket = new DatagramSocket()) {
                    socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                    return socket.getLocalAddress().getHostAddress();
                } catch (Exception e) {
                    return GlobalConstants.UNKNOWN;
                }
            }).onSuccess(ip -> {

                SystemConfig config = Injector.get(SystemConfig.class);

                JsonObject cluster = new JsonObject();

                cluster.put(Cluster.NAME, config.getModuleID())
                        .put(Cluster.CLUSTER_ID, clusterID)
                        .put(Cluster.CLUSTER_TYPE, memberType().toString())
                        .put(Cluster.IP_ADDRESS, ip);

                vertx.eventBus().send(ClusterAddress.REGISTER_EVENT.address(), cluster);

                vertx.setPeriodic(HEARTBEAT_INTERVAL_MS, x -> vertx.eventBus().send(ClusterAddress.HEARTBEAT.address(), cluster));

                promise.complete();

            }).onFailure(promise::fail);

        } catch (Exception e) {
            promise.fail(e);
        }

        return promise.future();
    }

    public final <T extends AbstractVerticle & Deployable> Future<Void> boot(Class<T> engine, DeploymentOptions options, boolean deploy) {
        if (deploy) {
            return deploy(engine.getCanonicalName(), null, options);
        }

        LOGGER.info(String.format("%s deploy skipped, note enabled", engine.getCanonicalName()));

        return Future.succeededFuture();
    }

    public final <T extends AbstractVerticle & Deployable> Future<Void> boot(Class<T> engine, DeploymentOptions options) {
        return deploy(engine.getCanonicalName(), null, options);
    }

    public final <T extends AbstractVerticle & Deployable> Future<Void> boot(T engine, boolean deploy) {
        if (deploy) {
            return deploy(engine.id(), engine, new DeploymentOptions());
        }

        LOGGER.info(String.format("%s deploy skipped, note enabled", engine.id()));

        return Future.succeededFuture();
    }

    public final <T extends AbstractVerticle & Deployable> Future<Void> boot(T engine) {
        return deploy(engine.id(), engine, new DeploymentOptions());
    }

    private Future<Void> deploy(String id, AbstractVerticle verticle, DeploymentOptions options) {

        Promise<Void> promise = promise();

        try {

            if (DEPLOYED_ENGINES.containsKey(id)) {

                LOGGER.warn(String.format("%s is already deployed", id));
                promise.fail(String.format("%s is already deployed", id));

            } else {

                options.setConfig(JsonObject.of(GlobalConstants.CLUSTER_ID, clusterID));

                (verticle == null
                        ?
                        vertx().deployVerticle(id, options)
                        :
                        vertx().deployVerticle(verticle, options))

                        .onSuccess(deploymentID -> {
                            DEPLOYED_ENGINES.put(id, deploymentID);
                            LOGGER.info(String.format("%s deployed", id));
                            promise.complete();
                        })
                        .onFailure(failure -> {
                            LOGGER.error(failure);
                            promise.fail(failure);
                        });
            }

        } catch (Exception e) {
            promise.fail(e);
            LOGGER.error(e);
        }

        return promise.future();
    }

    public final Future<Void> reboot(String engineID) {
        return Future.succeededFuture();
    }

    public final Future<Void> shutdown(String engineID) {
        return Future.succeededFuture();
    }


    protected static void exit(Throwable throwable) {
        throwable.printStackTrace();
        System.exit(-1);
    }

}
