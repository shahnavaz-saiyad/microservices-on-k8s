package com.sales.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WelcomeController {

    @Value("${message.from:Default Message}")
    private String messageFrom;

    private final DiscoveryClient discoveryClient;

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
