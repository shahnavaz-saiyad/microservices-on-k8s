package com.user.kafka;

import com.common.config.DynamicRoutingDataSource;
import com.common.config.TenantDataSourceConfig;
import com.common.dto.DecryptedDatasource;
import com.common.entity.master.Tenant;
import com.common.repository.master.TenantRepository;
import com.common.util.EncryptionUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Encryption;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TenantConsumer {

    private final TenantRepository tenantRepository;
    private final EncryptionUtility encryptionUtility;
    private final Environment environment;
    private final DynamicRoutingDataSource dynamicDataSource;

    @KafkaListener(topics = "initialize-tenant-datasource", groupId = "user-service")
    public void initializeTenantDatasource(String tenantUuid){
        Optional<Tenant> tenantOptional = tenantRepository.findByTenantUuid(tenantUuid);
        if(tenantOptional.isPresent()){
            try{
                Tenant tenant = tenantOptional.get();
                DecryptedDatasource decryptedDatasource = encryptionUtility.decryptDataSource(tenant);

                String driverClassName = environment.getProperty("tenant.datasource.driver-class-name."+decryptedDatasource.getDataSourcePlatform());

                DataSource dataSource = TenantDataSourceConfig.createDataSourceForTenant(decryptedDatasource, driverClassName);

                dynamicDataSource.addTargetDataSources(tenant.getTenantUuid(), dataSource);
                dynamicDataSource.initialize();

            }catch (Exception e){
                log.error(e.getMessage(), e);
            }

        }
    }
}
