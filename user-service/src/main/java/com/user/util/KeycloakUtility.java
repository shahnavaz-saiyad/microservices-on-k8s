package com.user.util;

import com.user.dto.KeycloakTokenRequest;
import com.user.dto.KeycloakUser;
import com.user.feign.KeycloakClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KeycloakUtility {

    private final KeycloakClient keycloakClient;

    @Value("${keycloak.clientId}")
    private String clientId;

    @Value("${keycloak.clientSecret}")
    private String clientSecret;

    public Map<String, Object> getAdminToken() {

        KeycloakTokenRequest tokenRequest = KeycloakTokenRequest.builder().grantType("client_credentials").clientId(clientId).clientSecret(clientSecret).build();
        return keycloakClient.getToken(tokenRequest);
    }


    public  Map<String, Object> createUser(KeycloakUser keycloakUser){
        String authToken = "Bearer "+ getAdminToken().get("access_token").toString();
        return keycloakClient.createUser(keycloakUser, authToken);
    }

}
