package com.user.kafka;

import com.common.config.DynamicRoutingDataSource;
import com.common.dto.KafkaPayload;
import com.common.dto.UserDto;
import com.common.util.KafkaUtility;
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

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j

public class UserConsumer {

    private final UserService userService;
    private final DynamicRoutingDataSource dynamicDatasource;
    private final KafkaUtility kafkaUtility;


    @KafkaListener(topics = "create-tenant-user", groupId = "create-tenant-user")
    public void createTenantUser(@Payload UserDto user, @Header("tenantUuid") String tenantUuid){

        dynamicDatasource.setCurrentTenant(tenantUuid);
        try{
            userService.createTenantUser(user);
            kafkaUtility.sendMessage(Map.of("status", "SUCCESS"), "create-tenant-user-status", tenantUuid);

        }catch(FeignException e){
            log.error(e.getMessage());
            kafkaUtility.sendMessage(Map.of("status", "FAILED"), "create-tenant-user-status", tenantUuid);
        }


        dynamicDatasource.clearCurrentTenant();
    }
}
