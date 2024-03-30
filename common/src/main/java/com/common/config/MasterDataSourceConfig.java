package com.common.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class MasterDataSourceConfig {


    private final Environment environment;

    @Bean
    @Primary
    public DataSourceInitializer dataSourceInitializer() {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(masterDataSource());

        return initializer;
    }

    @Bean
    @Primary
    public DataSource masterDataSource() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(environment.getProperty("master.datasource.url"));
        config.setUsername(environment.getProperty("master.datasource.username"));
        config.setPassword(environment.getProperty("master.datasource.password"));
        config.setDriverClassName(environment.getProperty("master.datasource.driver-class-name"));

        DataSource dataSource = new HikariDataSource(config);
        FlywayConfig.migrateDataSource(dataSource, "master");
        return dataSource;
    }

    @Bean
    @Primary
    public JdbcTemplate masterJdbcTemplate(){
        return new JdbcTemplate(masterDataSource());
    }


}
