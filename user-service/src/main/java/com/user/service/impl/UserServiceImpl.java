package com.user.service.impl;

import com.common.dto.UserDto;
import com.common.entity.tenant.User;
import com.common.repository.tenant.UserRepository;
import com.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void createTenantUser(UserDto userDto){

        User user = convertDtoToEntity(userDto);
        userRepository.save(user);
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
