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
    @Column(nullable = false)
    private String name;
    private String address;
    private String phoneNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "service_center_id")
    private ServiceCenter serviceCenter;

    public Provider(ServiceCenter serviceCenter, String name, String address, String phoneNumber) {
        this.serviceCenter = serviceCenter;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
