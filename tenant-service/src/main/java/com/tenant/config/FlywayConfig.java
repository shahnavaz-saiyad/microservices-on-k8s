package com.tenant.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class FlywayConfig {

    private final DynamicRoutingDataSource dynamicDataSource;

    @PostConstruct
    public void migrate() {
        dynamicDataSource.targetDataSources().entrySet().stream().filter((entry) -> !entry.getKey().equals("master"))
                .forEach((entry) -> migrateDataSource((DataSource) entry.getValue(), "mysql"));
    }

    public void migrateDataSource(DataSource dataSource, String databasePlatform) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db/migration/" + databasePlatform)
                .load();
        flyway.migrate();
    }
}
