package com.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class DecryptedDatasource {

    private String tenantUuid;

    private String dataSourceUrl;

    private String dataSourceUsername;

    private String dataSourcePassword;

    private String dataSourcePlatform;
}
