package com.tenant.controller;


import com.common.config.DynamicRoutingDataSource;
import com.common.entity.tenant.Product;
import com.common.repository.tenant.ProductRepository;
import com.tenant.config.ConfigMap;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WelcomeController {

    @Value("${message.from:Default Message}")
    private String messageFrom;

    private final DiscoveryClient discoveryClient;
    private final DynamicRoutingDataSource dynamicRoutingDataSource;
    private final ProductRepository productRepository;
    private final ConfigMap configMap;


    @PostConstruct
    public void postConstruct(){
        System.out.println("message from  "+messageFrom+ " "+ configMap.getFrom());

        System.out.println("Services:  " + discoveryClient.getServices());
    }

    @GetMapping
    public String welcome(){
        return "Welcome from "+ messageFrom+ " "+ configMap.getFrom();
    }


    @PostMapping("/products/{tenantUuid}")
    public ResponseEntity<?> addProduct(@PathVariable String tenantUuid, @RequestBody Product product){
        dynamicRoutingDataSource.setCurrentTenant(tenantUuid);
        productRepository.save(product);
        dynamicRoutingDataSource.clearCurrentTenant();
        return ResponseEntity.ok().build();
    }

}
