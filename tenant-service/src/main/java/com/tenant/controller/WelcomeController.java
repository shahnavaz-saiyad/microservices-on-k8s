package com.tenant.controller;

import com.tenant.config.DynamicRoutingDataSource;
import com.tenant.entity.tenant.Product;
import com.tenant.repository.tenant.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WelcomeController {

    @Value("${message.from:Default Message}")
    private String messageFrom;

    private final DiscoveryClient discoveryClient;
    private final ProductRepository productRepository;
    private final DynamicRoutingDataSource dynamicRoutingDataSource;

    @PostConstruct
    public void postConstruct(){
        System.out.println("message from  "+messageFrom);

        System.out.println("Services:  " + discoveryClient.getServices());
    }

    @GetMapping
    public String welcome(){
        return "Welcome from "+ messageFrom;
    }


    @PostMapping("/products/{tenantUuid}")
    public ResponseEntity<?> addProduct(@PathVariable String tenantUuid, @RequestBody Product product){
        dynamicRoutingDataSource.setCurrentTenant(tenantUuid);
        productRepository.save(product);
        dynamicRoutingDataSource.clearCurrentTenant();
        return ResponseEntity.ok().build();
    }

}
