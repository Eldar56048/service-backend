package com.crm.servicebackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "product_incoming_histories")
@Data
@NoArgsConstructor
public class IncomingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(nullable = false, name = "provider_id")
    private Provider provider;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private Date date;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "service_center_id")
    private ServiceCenter serviceCenter;

    public IncomingHistory(Product product, Provider provider, int quantity, int price, ServiceCenter serviceCenter) {
        this.product = product;
        this.provider = provider;
        this.quantity = quantity;
        this.price = price;
        this.date = new Date();
        this.serviceCenter = serviceCenter;
    }

}
