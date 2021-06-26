package com.crm.servicebackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "service_centers")
public class ServiceCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private boolean isEnabled;
    @Column(nullable = false)
    private String comment;

    public ServiceCenter(String name, String address, String phoneNumber, boolean isEnabled, String comment) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.isEnabled = isEnabled;
        this.comment = comment;
    }
}
