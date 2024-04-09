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

    @Column(name = "status")
    private String status;

    @Column(name = "encrypted_datasource")
    private String encryptedDataSource;
}
