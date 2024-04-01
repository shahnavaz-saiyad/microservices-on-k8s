package com.tenant.controller;

import com.common.config.DynamicRoutingDataSource;
import com.common.config.FlywayConfig;
import com.common.config.TenantDataSourceConfig;
import com.common.dto.DecryptedDatasource;
import com.common.entity.master.Tenant;
import com.common.repository.master.TenantRepository;
import com.common.util.EncryptionUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
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
    private final EncryptionUtility encryptionUtility;


    @PostMapping
    public ResponseEntity<?> registerTenant(@RequestBody Tenant tenant) throws Exception {

        DecryptedDatasource decryptedDatasource = encryptionUtility.decryptDataSource(tenant);

        String driverClassName = environment.getProperty("tenant.datasource.driver-class-name."+decryptedDatasource.getDataSourcePlatform());

        DataSource dataSource = TenantDataSourceConfig.createDataSourceForTenant(decryptedDatasource, driverClassName);

        dynamicDataSource.addTargetDataSources(tenant.getTenantUuid(), dataSource);
        dynamicDataSource.initialize();
        flywayConfig.migrateDataSource(dataSource, decryptedDatasource.getDataSourcePlatform());
        tenantRepository.save(tenant);


        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Tenant>> getAll(){
        List<Tenant> tenants = tenantRepository.findAll();
        return ResponseEntity.ok(tenants);
    }


}
