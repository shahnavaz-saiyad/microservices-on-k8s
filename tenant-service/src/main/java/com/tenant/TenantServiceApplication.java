package com.tenant;

import com.common.CommonApplication;
import com.common.repository.tenant.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.common.*", "com.tenant.*"})
public class TenantServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(TenantServiceApplication.class, args);
    }

}
