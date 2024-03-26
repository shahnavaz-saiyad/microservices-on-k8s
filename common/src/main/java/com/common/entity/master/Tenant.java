package com.common.entity.master;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tenant")
public class Tenant implements Serializable {

    @Id
    @Column(name = "tenant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tenantId;

    @Column(name = "tenant_uuid")
    private String tenantUuid;

    @Column(name = "tenant_name")
    private String tenantName;

    @Column(name = "tenant_description")
    private String tenantDescription;

    @Column(name = "data_source_url")
    private String dataSourceUrl;

    @Column(name = "data_source_username")
    private String dataSourceUsername;

    @Column(name = "data_source_password")
    private String dataSourcePassword;

    @Column(name = "data_source_platform")
    private String dataSourcePlatform;
}
