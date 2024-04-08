package com.tenant.service.impl;

import com.common.config.DynamicRoutingDataSource;
import com.common.config.FlywayConfig;
import com.common.config.TenantDataSourceConfig;
import com.common.dto.DecryptedDatasource;
import com.common.entity.master.Tenant;
import com.common.repository.master.TenantRepository;
import com.common.util.EncryptionUtility;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.tenant.dto.TenantDto;
import com.tenant.service.TenantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.jose4j.lang.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {


    private final TenantRepository tenantRepository;
    private final DynamicRoutingDataSource dynamicDataSource;
    private final Environment environment;
    private final EncryptionUtility encryptionUtility;

    @Override
    public void registerTenant(TenantDto tenantDto) throws Exception {

        Tenant tenant = dtoToEntity(tenantDto);
        DecryptedDatasource decryptedDatasource = encryptionUtility.decryptDataSource(tenant);

        String driverClassName = environment.getProperty("tenant.datasource.driver-class-name."+decryptedDatasource.getDataSourcePlatform());

        DataSource dataSource = TenantDataSourceConfig.createDataSourceForTenant(decryptedDatasource, driverClassName);

        dynamicDataSource.addTargetDataSources(tenant.getTenantUuid(), dataSource);
        dynamicDataSource.initialize();
        FlywayConfig.migrateDataSource(dataSource, decryptedDatasource.getDataSourcePlatform());
        tenantRepository.save(tenant);

    }

    @Override
    public List<TenantDto> findAll() {
        List<Tenant> tenants = tenantRepository.findAll();
        return tenants.stream().map(this::entityToDto).toList();
    }

    private TenantDto entityToDto(Tenant tenant) {
        TenantDto tenantDto = new TenantDto();
        BeanUtils.copyProperties(tenant, tenantDto);
        return tenantDto;
    }

    private Tenant dtoToEntity(TenantDto tenantDto) {
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(tenantDto, tenant);

        if(StringUtils.isBlank(tenant.getTenantUuid())){
            tenant.setTenantUuid(UUID.randomUUID().toString());
        }
        return tenant;
    }
}
