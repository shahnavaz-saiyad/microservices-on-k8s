package com.tenant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.common.*", "com.tenant.*"})
@EnableAsync
public class TenantServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(TenantServiceApplication.class, args);
    }

}
