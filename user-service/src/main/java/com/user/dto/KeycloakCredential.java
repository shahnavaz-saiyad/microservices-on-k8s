package com.user.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class KeycloakCredential implements Serializable {

    private Boolean temporary;
    private String type;
    private String value;

}
