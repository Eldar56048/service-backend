package com.crm.servicebackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private int price;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "service_center_id")
    private ServiceCenter serviceCenter;

    public Product(ServiceCenter serviceCenter, String name, String description, int price) {
        this.serviceCenter = serviceCenter;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
