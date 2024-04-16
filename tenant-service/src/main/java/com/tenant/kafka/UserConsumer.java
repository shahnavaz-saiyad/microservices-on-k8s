package com.tenant.kafka;

import com.common.config.DynamicRoutingDataSource;
import com.common.repository.master.TenantRepository;
import com.tenant.util.SseUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {

    private final TenantRepository tenantRepository;
    private final SseUtility sseUtility;
    private final DynamicRoutingDataSource dynamicDataSource;

    @KafkaListener(topics = "create-tenant-user-status", groupId = "create-tenant-user-status")
    public void createTenantUser(@Payload Map<String, Object> request, @Header("tenantUuid") String tenantUuid){

        tenantRepository.findByTenantUuid(tenantUuid).ifPresent(tenant -> {

            SseEmitter emitter = sseUtility.getEmitter(tenantUuid);

            if(request.get("status") != null && request.get("status").equals("SUCCESS")){
               tenant.setStatus("ACTIVE");
               tenantRepository.save(tenant);
               try {
                   emitter.send(Map.of("type", "success", "message", "Registration completed"));
                   sseUtility.removeEmitter(tenantUuid);
               } catch (IOException e) {
                   log.error(e.getMessage(), e);
               }
           }
           else{
               tenantRepository.delete(tenant);
                try {
                    emitter.send(Map.of("type", "error", "message", "User creation failed!"));
                    sseUtility.removeEmitter(tenantUuid);
                    dynamicDataSource.removeDataSource(tenantUuid);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
           }
        });
    }


}
