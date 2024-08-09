
package com.oi.sdk.cmdb.store;

import com.oi.sdk.cmdb.ConfigCollection;
import com.oi.sdk.cmdb.entity.DruidSupervisor;

/**
 * A store for managing Druid Supervisor entities.
 * <p>
 * This class extends AbstractStore.
 *

 */
public class DruidSupervisorStore extends AbstractStore {

    private DruidSupervisorStore() {
    }

    private static DruidSupervisorStore INSTANCE;

    public static DruidSupervisorStore get() {
        if (INSTANCE == null) {
            INSTANCE = new DruidSupervisorStore();
        }
        return INSTANCE;
    }

    @Override
    public String collection() {
        return ConfigCollection.DRUID_SUPERVISOR.getName();
    }

    @Override
    public DruidSupervisor entity() {
        return new DruidSupervisor();
    }

}
