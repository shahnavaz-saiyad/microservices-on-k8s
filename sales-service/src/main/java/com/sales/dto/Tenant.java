package com.sales.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Tenant {

    private String tenantUuid;

    private String dataSourceUrl;

    private String dataSourceUsername;

    private String dataSourcePassword;

    private String dataSourceDriverClassName;
}
