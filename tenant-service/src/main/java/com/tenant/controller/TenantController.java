package com.tenant.controller;

import com.common.config.DynamicRoutingDataSource;
import com.common.config.FlywayConfig;
import com.common.config.TenantDataSourceConfig;
import com.common.entity.master.Tenant;
import com.common.repository.master.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.List;

@RestController
@RequestMapping("/tenants/")
@RequiredArgsConstructor
public class TenantController {
    private final TenantRepository tenantRepository;
    private final DynamicRoutingDataSource dynamicDataSource;
    private final TenantDataSourceConfig tenantDataSourceConfig;
    private final FlywayConfig flywayConfig;
    private final Environment environment;

    @PostMapping
    public ResponseEntity<?> registerTenant(@RequestBody Tenant tenant){
        String driverClassName = environment.getProperty("tenant.datasource.driver-class-name."+tenant.getDataSourcePlatform());

        DataSource dataSource = tenantDataSourceConfig.createDataSourceForTenant(tenant, driverClassName);
        dynamicDataSource.addTargetDataSources(tenant.getTenantUuid(), dataSource);
        dynamicDataSource.initialize();
        flywayConfig.migrateDataSource(dataSource, tenant.getDataSourcePlatform());
        tenantRepository.save(tenant);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Tenant>> getAll(){
        List<Tenant> tenants = tenantRepository.findAll();
        return ResponseEntity.ok(tenants);
    }
}
