package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.Site;

public class SiteStore extends AbstractStore {

    private SiteStore() {
    }

    private static SiteStore INSTANCE;

    public static SiteStore get() {
        if (INSTANCE == null) {
            INSTANCE = new SiteStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.SITE.getName();
    }

    @Override
    public Site entity() {
        return new Site();
    }
}
