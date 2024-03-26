package com.userservice.controller;

import com.common.config.DynamicRoutingDataSource;
import com.common.entity.tenant.Product;
import com.common.repository.tenant.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/welcome")
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

}
