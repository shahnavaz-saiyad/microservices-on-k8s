package com.common.config;

import com.common.entity.master.Tenant;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.NamedNativeQuery;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class FlywayConfig {

    private final JdbcTemplate masterJdbcTemplate;
    private final Environment environment;

    @PostConstruct
    public void migrate() {
        List<Tenant> tenants = masterJdbcTemplate.query("SELECT * from tenant", TenantDataSourceConfig::convertTenants);

        for (Tenant tenant : tenants) {
            String driverClassName = environment.getProperty("tenant.datasource.driver-class-name."+tenant.getDataSourcePlatform());

            DataSource dataSource = TenantDataSourceConfig.createDataSourceForTenant(tenant, driverClassName);
            migrateDataSource(dataSource, tenant.getDataSourcePlatform());
        }

//        dynamicDataSource.targetDataSources().entrySet().stream().filter((entry) -> !entry.getKey().equals("master"))
//                .forEach((entry) -> migrateDataSource((DataSource) entry.getValue(), "mysql"));
    }

    public void migrateDataSource(DataSource dataSource, String databasePlatform) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db/migration/" + databasePlatform)
                .load();
        flyway.migrate();
    }
}
