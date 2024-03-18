package com.sales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@EnableDiscoveryClient
public class SalesServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(SalesServiceApplication.class, args);
    }

}
