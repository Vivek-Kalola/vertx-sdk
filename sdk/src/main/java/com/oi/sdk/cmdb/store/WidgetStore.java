package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.Widget;

public class WidgetStore extends AbstractStore {

    private WidgetStore() {
    }

    private static WidgetStore INSTANCE;

    public static WidgetStore get() {
        if (INSTANCE == null) {
            INSTANCE = new WidgetStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.WIDGET.getName();
    }

    @Override
    public Widget entity() {
        return new Widget();
    }
}
