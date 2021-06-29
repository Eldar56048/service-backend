package com.crm.servicebackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "types")
@Data
@NoArgsConstructor
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "service_center_id")
    private ServiceCenter serviceCenter;

    public Type(String name, ServiceCenter serviceCenter){
        this.name = name;
        this.serviceCenter = serviceCenter;
    }

}
