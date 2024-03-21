package com.tenant.controller;

import com.tenant.config.DynamicRoutingDataSource;
import com.tenant.config.TenantDataSourceConfig;
import com.tenant.entity.master.Tenant;
import com.tenant.repository.master.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tenants/")
@RequiredArgsConstructor
public class TenantController {
    private final TenantRepository tenantRepository;
    private final DynamicRoutingDataSource dynamicDataSource;
    private final TenantDataSourceConfig tenantDataSourceConfig;

    @PostMapping
    public ResponseEntity<?> registerTenant(@RequestBody Tenant tenant){

        dynamicDataSource.addTargetDataSources(tenant.getTenantUuid(), tenantDataSourceConfig.createDataSourceForTenant(tenant));
        dynamicDataSource.initialize();
        tenantRepository.save(tenant);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Tenant>> getAll(){
        List<Tenant> tenants = tenantRepository.findAll();
        return ResponseEntity.ok(tenants);
    }
}
