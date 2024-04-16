package com.user.feign;

import com.user.dto.KeycloakTokenRequest;
import com.user.dto.KeycloakUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "keycloak", url = "${keycloak.url}")
public interface KeycloakClient {

    @PostMapping(value = "/realms/ims/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Map<String, Object> getToken(KeycloakTokenRequest tokenRequest);

    @PostMapping(value = "/admin/realms/ims/users",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> createUser(@RequestBody KeycloakUser keycloakUser, @RequestHeader String authorization);
}
