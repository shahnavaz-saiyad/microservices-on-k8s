package com.common.repository.master;

import com.common.entity.master.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByTenantUuid(String tenantUuid);
}
