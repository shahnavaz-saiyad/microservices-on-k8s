package com.tenant.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public void setCurrentTenant(String tenantUuid) {
        this.currentTenant.set(tenantUuid);
    }

    public void clearCurrentTenant() {
        this.currentTenant.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        // logic to determine the datasource key based on tenantUuid
        return currentTenant.get();
    }


}

