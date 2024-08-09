package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.SNMPTemplate;

public class SNMPTemplateStore extends AbstractStore {

    private SNMPTemplateStore() {
    }

    private static SNMPTemplateStore INSTANCE;

    public static SNMPTemplateStore get() {
        if (INSTANCE == null) {
            INSTANCE = new SNMPTemplateStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.SNMP_TEMPLATE.getName();
    }

    @Override
    public SNMPTemplate entity() {
        return new SNMPTemplate();
    }
}
