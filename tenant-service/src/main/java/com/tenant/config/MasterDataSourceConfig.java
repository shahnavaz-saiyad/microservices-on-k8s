package com.tenant.config;

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
        System.out.println("Datasource url: "+ environment.getProperty("master.datasource.url"));
        config.setJdbcUrl(environment.getProperty("master.datasource.url"));
        config.setUsername(environment.getProperty("master.datasource.username"));
        config.setPassword(environment.getProperty("master.datasource.password"));
        config.setDriverClassName(environment.getProperty("master.datasource.driver-class-name"));



        return new HikariDataSource(config);
    }

    @Bean
    @Primary
    public JdbcTemplate masterJdbcTemplate(){
        return new JdbcTemplate(masterDataSource());
    }


//    private DatabasePopulator databasePopulator() {
//        // define database populator if needed
//    }
}
