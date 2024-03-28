package com.tenant.controller;

import com.common.config.DynamicRoutingDataSource;
import com.common.config.FlywayConfig;
import com.common.config.TenantDataSourceConfig;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
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
    public ResponseEntity<?> registerTenant(@RequestBody Tenant tenant) throws Exception {
        String driverClassName = environment.getProperty("tenant.datasource.driver-class-name."+tenant.getDataSourcePlatform());

        DataSource dataSource = tenantDataSourceConfig.createDataSourceForTenant(tenant, driverClassName);
        dynamicDataSource.addTargetDataSources(tenant.getTenantUuid(), dataSource);
        dynamicDataSource.initialize();
        flywayConfig.migrateDataSource(dataSource, tenant.getDataSourcePlatform());
        tenantRepository.save(tenant);

        byte[] encryptedText = EncryptionUtility.decodeFromString(tenant.getEncryptedDataSource());
        String decrypt = EncryptionUtility.decrypt(encryptedText, loadPrivateKey());
        System.out.println(decrypt);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Tenant>> getAll(){
        List<Tenant> tenants = tenantRepository.findAll();
        return ResponseEntity.ok(tenants);
    }

    public static PrivateKey loadPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        ClassPathResource classPathResource = new ClassPathResource("private.key");
        InputStream inputStream = classPathResource.getInputStream();
//        FileInputStream fis = new FileInputStream(new File("encryption-decryption/encryption-keys", filename));
        byte[] encodedPrivateKey = new byte[inputStream.available()];
        inputStream.read(encodedPrivateKey);
        inputStream.close();

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        return keyFactory.generatePrivate(privateKeySpec);
    }
}
