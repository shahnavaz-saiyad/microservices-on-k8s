package com.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserDto implements Serializable {

    private String userUuid;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;

}
