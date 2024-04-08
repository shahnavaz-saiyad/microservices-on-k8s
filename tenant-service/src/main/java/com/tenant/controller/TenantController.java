package com.tenant.controller;

import com.common.config.DynamicRoutingDataSource;
import com.common.config.FlywayConfig;
import com.common.config.TenantDataSourceConfig;
import com.common.dto.DecryptedDatasource;
import com.common.entity.master.Tenant;
import com.common.repository.master.TenantRepository;
import com.common.util.EncryptionUtility;
import com.tenant.dto.TenantDto;
import com.tenant.service.TenantService;
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
    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<?> registerTenant(@RequestBody TenantDto tenant) throws Exception {

        tenantService.registerTenant(tenant);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TenantDto>> getAll(){
        List<TenantDto> tenants = tenantService.findAll();
        return ResponseEntity.ok(tenants);
    }


}
