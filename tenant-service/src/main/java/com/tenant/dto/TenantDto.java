package com.tenant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class TenantDto implements Serializable {

    private String tenantUuid;
    private String tenantName;
    private String tenantDescription;
    private String encryptedDataSource;
}
