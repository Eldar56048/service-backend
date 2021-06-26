package com.crm.servicebackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "providers")
@Data
@NoArgsConstructor
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "service_center_id")
    private ServiceCenter serviceCenter;
    @Column(nullable = false)
    private String providerName;
    private String providerAddress;
    private String providerTelephone;

    public Provider(ServiceCenter serviceCenter, String providerName, String providerAddress, String providerTelephone) {
        this.serviceCenter = serviceCenter;
        this.providerName = providerName;
        this.providerAddress = providerAddress;
        this.providerTelephone = providerTelephone;
    }
}
