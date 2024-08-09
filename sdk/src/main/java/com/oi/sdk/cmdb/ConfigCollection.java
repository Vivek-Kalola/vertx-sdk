package com.oi.sdk.cmdb;


import com.oi.sdk.cmdb.entity.*;

/**
 * This is very important class, as it take care of Config DB structure
 * It Also tightly associated with AuthProvider, Whenever you create new collection, new APIs... You have to update AuthProvider.java
 *
 * @author Vvek
 */
public enum ConfigCollection {

    AUTH_TOKEN("auth-token", null, AuthToken.class),
    CREDENTIAL_PROFILE("credential-profile", "credential-profile.json", CredentialProfile.class),
    CLUSTER("cluster", null, Cluster.class),
    DASHBOARD("dashboard", "dashboard.json", Dashboard.class),
    DEVICE("device", null, Device.class),
    DEVICE_MANAGER("device-manager", null, DeviceManager.class),
    DISCOVERY_SCHEDULER("discovery-scheduler", "discovery-scheduler.json", DiscoveryScheduler.class),
    DISCOVERY_CONTEXT("discovery-context", null, null),
    DRUID_SUPERVISOR("druid-supervisor-specs", "druid-supervisor-specs.json", DruidSupervisor.class),
    GROUP("group", "group.json", Group.class),
    OBJECT("object", null, DeviceObject.class),
    POLICY("policy", "policy.json", Policy.class),
    ROLE("role", "role.json", Role.class),
    SITE("site", "site.json", Site.class),
    SMTP_SERVER("smtp-server", "smtp-server.json", SMPTServer.class),
    SNMP_CATALOG("snmp-catalog", "snmp-catalog.json", SNMPCatalog.class),
    SNMP_TEMPLATE("snmp-template", "snmp-template.json", SNMPTemplate.class),
    SYSTEM("system", null, null),
    SCHEDULER("scheduler", "scheduler.json", null),
    USER("user", "user.json", User.class),
    VERSION("version", null, null),
    WIDGET("widget", null, Widget.class);

    private final String name;                      // collection name: Synced with Config DB
    private final String schema;                    // schema files: to load default values
    private final Class<? extends Entity> entity;    // Entity Class for indexing and validation

    ConfigCollection(String name, String schema, Class<? extends Entity> entity) {
        this.name = name;
        this.schema = schema;
        this.entity = entity;
    }

    public String getName() {
        return name;
    }

    public String getSchema() {
        return schema;
    }

    public Class<? extends Entity> getEntity() {
        return entity;
    }

    public boolean loadDefaults() {
        return getSchema() != null;
    }

}


