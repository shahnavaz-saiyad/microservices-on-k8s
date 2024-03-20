package com.tenant.config;

import com.tenant.entity.master.Tenant;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@EnableJpaRepositories(basePackages = {"com.tenant.repository.tenant", "com.tenant.repository.master"}, transactionManagerRef = "transcationManager", entityManagerFactoryRef = "entityManager")
@EnableTransactionManagement
public class TenantDataSourceConfig {


    private final DataSource masterDataSource;
    private final JdbcTemplate masterJdbcTemplate;

    @Bean(name = "entityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dynamicDataSource()).packages("com.tenant.entity.tenant", "com.tenant.entity.master").build();
    }

    @Bean(name = "transcationManager")
    public JpaTransactionManager transactionManager(
            @Autowired @Qualifier("entityManager") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return new JpaTransactionManager(entityManagerFactoryBean.getObject());
    }


    @Bean
    public DynamicRoutingDataSource dynamicDataSource() {

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("master", masterDataSource);
        List<Tenant> tenants = masterJdbcTemplate.query("SELECT * from tenant", this::convertTenants);

        for (Tenant tenant : tenants) {
            DataSource dataSource = createDataSourceForTenant(tenant);
            targetDataSources.put(tenant.getTenantUuid(), dataSource);
        }

        DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);
        return routingDataSource;
    }



    private DataSource createDataSourceForTenant(Tenant tenant) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(tenant.getDataSourceUrl());
        config.setUsername(tenant.getDataSourceUsername());
        config.setPassword(tenant.getDataSourcePassword());
        config.setDriverClassName(tenant.getDataSourceDriverClassName());


        return new HikariDataSource(config);
    }

    private Tenant convertTenants(ResultSet resultSet, int i) throws SQLException {
        Tenant tenant = new Tenant();
        tenant.setTenantUuid(resultSet.getString("tenant_uuid"));
        tenant.setDataSourceUrl(resultSet.getString("data_source_url"));
        tenant.setDataSourceUsername(resultSet.getString("data_source_username"));
        tenant.setDataSourcePassword(resultSet.getString("data_source_password"));
        tenant.setDataSourceDriverClassName(resultSet.getString("data_source_driver_class_name"));
        return tenant;
    }

}
