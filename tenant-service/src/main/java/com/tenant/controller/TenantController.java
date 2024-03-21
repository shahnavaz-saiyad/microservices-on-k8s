package com.tenant.controller;

import com.tenant.config.DynamicRoutingDataSource;
import com.tenant.config.FlywayConfig;
import com.tenant.config.TenantDataSourceConfig;
import com.tenant.entity.master.Tenant;
import com.tenant.repository.master.TenantRepository;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public ResponseEntity<?> registerTenant(@RequestBody Tenant tenant){

        DataSource dataSource = tenantDataSourceConfig.createDataSourceForTenant(tenant);
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
