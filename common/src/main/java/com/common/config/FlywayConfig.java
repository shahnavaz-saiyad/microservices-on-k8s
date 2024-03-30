package com.common.config;

import com.common.dto.DecryptedDatasource;
import com.common.entity.master.Tenant;
import com.common.util.EncryptionUtility;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class FlywayConfig {

    private final JdbcTemplate masterJdbcTemplate;
    private final Environment environment;

    @PostConstruct
    public void migrate() throws Exception {
        List<Tenant> tenants = masterJdbcTemplate.query("SELECT * from tenant", TenantDataSourceConfig::convertTenants);

        for (Tenant tenant : tenants) {
            DecryptedDatasource decryptedDatasource = EncryptionUtility.decryptDataSource(tenant);
            String driverClassName = environment.getProperty("tenant.datasource.driver-class-name."+decryptedDatasource.getDataSourcePlatform());

            DataSource dataSource = TenantDataSourceConfig.createDataSourceForTenant(decryptedDatasource, driverClassName);
            migrateDataSource(dataSource, decryptedDatasource.getDataSourcePlatform());
        }

//        dynamicDataSource.targetDataSources().entrySet().stream().filter((entry) -> !entry.getKey().equals("master"))
//                .forEach((entry) -> migrateDataSource((DataSource) entry.getValue(), "mysql"));
    }

    public static void migrateDataSource(DataSource dataSource, String databasePlatform) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db/migration/" + databasePlatform)
                .load();
        flyway.migrate();
    }
}
