package com.user.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class KeycloakUser implements Serializable {

    private String username;
    private Boolean enabled;
    private String firstName;
    private String lastName;
    private String email;
    private List<KeycloakCredential> credentials;

}
