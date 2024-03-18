package com.userservice.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WelcomeController {

    @Value("${message.from:Default Message}")
    private String messageFrom;

    private final DiscoveryClient discoveryClient;

    @PostConstruct
    public void postConstruct(){
        System.out.println("message from  "+messageFrom);
        System.out.println(discoveryClient.getInstances("user-service"));
    }

    @GetMapping
    public String welcome(){
        return "Welcome from "+ messageFrom;
    }

    @RequestMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(
            @PathVariable String applicationName) {

        return discoveryClient.getInstances(applicationName);

    }
}
