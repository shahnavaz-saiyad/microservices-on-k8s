package com.user.service.impl;

import com.common.dto.UserDto;
import com.common.entity.tenant.User;
import com.common.repository.tenant.UserRepository;
import com.user.dto.KeycloakCredential;
import com.user.dto.KeycloakUser;
import com.user.service.UserService;
import com.user.util.KeycloakUtility;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KeycloakUtility keycloakUtility;

    @Override
    public void createTenantUser(UserDto userDto){

        User user = convertDtoToEntity(userDto);
        userRepository.save(user);

        KeycloakUser keycloakUser = userToKeyCloakUser(user);
        keycloakUtility.createUser(keycloakUser);

    }

    private KeycloakUser userToKeyCloakUser(User user) {
        return KeycloakUser.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .enabled(true)
                .credentials(List.of(
                        KeycloakCredential.builder()
                                .temporary(false)
                                .type("password")
                                .value(user.getPassword())
                                .build()
                ))
                .build();
    }

    private User convertDtoToEntity(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        if(StringUtils.isEmpty(user.getUserUuid())){
            user.setUserUuid(UUID.randomUUID().toString());
        }
        return user;
    }
}
