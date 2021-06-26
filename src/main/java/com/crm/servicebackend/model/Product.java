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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "service_center_id")
    private ServiceCenter serviceCenter;
    @Column(nullable = false)
    private String productName;
    private String description;
    @Column(nullable = false)
    private int price;

    public Product(ServiceCenter serviceCenter, String productName, String description, int price) {
        this.serviceCenter = serviceCenter;
        this.productName = productName;
        this.description = description;
        this.price = price;
    }
}
