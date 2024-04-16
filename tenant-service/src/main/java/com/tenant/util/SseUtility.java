package com.tenant.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SseUtility {
    public Map<String, SseEmitter> emitters = new HashMap<>();

    public void addEmitter(String tenantUuid, SseEmitter emitter) {
        emitters.put(tenantUuid, emitter);
    }

    public SseEmitter getEmitter(String tenantUuid){
        return emitters.get(tenantUuid);
    }

    public void removeEmitter(String tenantUuid) {
        getEmitter(tenantUuid).complete();
        emitters.remove(tenantUuid);
    }
}
