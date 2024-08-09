package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.SNMPCatalog;

public class SNMPCatalogStore extends AbstractStore {

    private SNMPCatalogStore() {
    }

    private static SNMPCatalogStore INSTANCE;

    public static SNMPCatalogStore get() {
        if (INSTANCE == null) {
            INSTANCE = new SNMPCatalogStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.SNMP_CATALOG.getName();
    }

    @Override
    public SNMPCatalog entity() {
        return new SNMPCatalog();
    }
}
