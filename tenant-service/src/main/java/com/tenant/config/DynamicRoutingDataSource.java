package com.tenant.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;


public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    private Map<Object, Object> targetDataSources;


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

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        this.targetDataSources = targetDataSources;
        super.setTargetDataSources(targetDataSources);
    }

    public void addTargetDataSources(Object tenantUuid, Object dataSource) {
        targetDataSources.put(tenantUuid, dataSource);
        super.setTargetDataSources(targetDataSources);
    }
}

