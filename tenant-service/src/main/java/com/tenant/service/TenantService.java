package com.tenant.service;

import com.tenant.dto.TenantDto;

import java.util.List;

public interface TenantService {

    public TenantDto registerTenant(TenantDto tenantDto) throws Exception;

    List<TenantDto> findAll();
}
