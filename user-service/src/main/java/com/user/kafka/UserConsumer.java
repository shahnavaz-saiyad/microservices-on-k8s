package com.user.kafka;

import com.common.config.DynamicRoutingDataSource;
import com.common.dto.KafkaPayload;
import com.common.dto.UserDto;
import com.user.service.UserService;
import com.user.util.KeycloakUtility;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j

public class UserConsumer {

    private final UserService userService;
    private final DynamicRoutingDataSource dynamicDatasource;


    @KafkaListener(topics = "create-tenant-user", groupId = "create-tenant-user")
    public void createTenantUser(@Payload UserDto user, @Header("tenantUuid") String tenantUuid){

        dynamicDatasource.setCurrentTenant(tenantUuid);
        try{
            userService.createTenantUser(user);

        }catch(FeignException e){
            log.error(e.getMessage());
        }


        dynamicDatasource.clearCurrentTenant();
    }
}
