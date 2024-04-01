package com.common.config;

import com.common.dto.DecryptedDatasource;
import com.common.entity.master.Tenant;
import com.common.util.EncryptionUtility;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = {"com.common.repository.tenant", "com.common.repository.master"}, transactionManagerRef = "transcationManager", entityManagerFactoryRef = "entityManager")
@EnableTransactionManagement
public class TenantDataSourceConfig {


    private final DataSource masterDataSource;
    private final JdbcTemplate masterJdbcTemplate;
    private final Environment environment;
    private final EncryptionUtility encryptionUtility;

    @Bean(name = "entityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder) throws Exception {
        return builder.dataSource(dynamicDataSource()).packages("com.common.entity.tenant", "com.common.entity.master").build();
    }

    @Bean(name = "transcationManager")
    public JpaTransactionManager transactionManager(
            @Autowired @Qualifier("entityManager") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return new JpaTransactionManager(entityManagerFactoryBean.getObject());
    }


    @Bean
    public DynamicRoutingDataSource dynamicDataSource() throws Exception {

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("master", masterDataSource);
        List<Tenant> tenants = masterJdbcTemplate.query("SELECT * from tenant", TenantDataSourceConfig::convertTenants);

        for (Tenant tenant : tenants) {
            DecryptedDatasource decryptedDatasource = encryptionUtility.decryptDataSource(tenant);
            String driverClassName = environment.getProperty("tenant.datasource.driver-class-name."+decryptedDatasource.getDataSourcePlatform());
            DataSource dataSource = createDataSourceForTenant(decryptedDatasource, driverClassName);
            targetDataSources.put(tenant.getTenantUuid(), dataSource);
        }

        DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);
        return routingDataSource;
    }



    public static DataSource createDataSourceForTenant(DecryptedDatasource decryptedDatasource, String driverClassName) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(decryptedDatasource.getDataSourceUrl());
        config.setUsername(decryptedDatasource.getDataSourceUsername());
        config.setPassword(decryptedDatasource.getDataSourcePassword());
        config.setDriverClassName(driverClassName);

        return new HikariDataSource(config);
    }

    public static Tenant convertTenants(ResultSet resultSet, int i) throws SQLException {
        Tenant tenant = new Tenant();
        tenant.setTenantUuid(resultSet.getString("tenant_uuid"));
        tenant.setEncryptedDataSource(resultSet.getString("encrypted_datasource"));
        return tenant;
    }

}
