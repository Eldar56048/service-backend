package com.crm.servicebackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "storage_items")
@Data
@NoArgsConstructor
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "service_center_id")
    private ServiceCenter serviceCenter;
    @ManyToOne
    @JoinColumn(nullable = false, name = "product_id")
    private Product product;
    @Column(nullable = false)
    private int quantity;

    public Storage(ServiceCenter serviceCenter, Product product, int quantity) {
        this.serviceCenter = serviceCenter;
        this.product = product;
        this.quantity = quantity;
    }
}
