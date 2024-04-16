package com.tenant.controller;

import com.tenant.dto.TenantDto;
import com.tenant.service.TenantService;
import com.tenant.util.SseUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/tenants/")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;
    private final SseUtility sseUtility;

    @PostMapping
    public ResponseEntity<TenantDto> registerTenant(@RequestBody TenantDto tenant) throws Exception {

        TenantDto tenantDto = tenantService.registerTenant(tenant);
        return ResponseEntity.ok(tenantDto);
    }

    @RequestMapping(value = "/{tenantUuid}/sse/subscribe")
    public SseEmitter subscribeSse(@PathVariable String tenantUuid){
        SseEmitter sse = new SseEmitter(86400000L);
        sseUtility.addEmitter(tenantUuid, sse);
        return sse;
    }

    @GetMapping
    public ResponseEntity<List<TenantDto>> getAll(){
        List<TenantDto> tenants = tenantService.findAll();
        return ResponseEntity.ok(tenants);
    }


}
