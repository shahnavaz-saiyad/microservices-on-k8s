package com.tenant.service;

import com.common.entity.master.Tenant;
import com.tenant.dto.TenantDto;

import java.util.List;

public interface TenantService {

    public void registerTenant(TenantDto tenantDto) throws Exception;

    List<TenantDto> findAll();
}
