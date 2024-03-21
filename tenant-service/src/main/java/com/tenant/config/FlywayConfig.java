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
                .forEach((entry) -> migrateDataSource((DataSource) entry.getValue()));
    }

    public void migrateDataSource(DataSource dataSource) {
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
    }
}
