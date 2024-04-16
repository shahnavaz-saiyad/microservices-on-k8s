package com.tenant.kafka;

import com.common.dto.UserDto;
import com.common.repository.master.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {

    private final TenantRepository tenantRepository;

    @KafkaListener(topics = "create-tenant-user-status", groupId = "create-tenant-user-status")
    public void createTenantUser(@Payload Map<String, Object> request, @Header("tenantUuid") String tenantUuid){

        tenantRepository.findByTenantUuid(tenantUuid).ifPresent(tenant -> {
           if(request.get("status") != null && request.get("status").equals("SUCCESS")){
               tenant.setStatus("ACTIVE");
               tenantRepository.save(tenant);
           }
           else{
               tenantRepository.delete(tenant);

           }
        });
    }


}
