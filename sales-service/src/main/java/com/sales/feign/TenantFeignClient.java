package com.sales.feign;

import com.sales.dto.Tenant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "tenantFeignClient")
public interface TenantFeignClient {

    @GetMapping("/tenants/")
    List<Tenant> getAll();
}
