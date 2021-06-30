package com.crm.servicebackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    private Long orderId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "service_center_id")
    private ServiceCenter serviceCenter;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "done_user_id")
    private User doneUser;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_experience_id")
    private ExperienceModel userExperience;
    @Column(nullable = false)
    private int soldPrice;
    @Column(nullable = false)
    private int quantity;
    private int servicePercentage;
    private int lastPrice;
    private int userExperienceCoefficient;

    public OrderItem(int quantity, Order order, Service service, User user) {
        this.orderId = order.getId();
        this.serviceCenter = serviceCenter;
        this.service = service;
        this.doneUser = user;
        this.userExperience = user.getExperienceModel();
        this.userExperienceCoefficient = user.getExperienceModel().getCoefficient();
        this.quantity = quantity;
        this.soldPrice = service.getPrice();
        this.servicePercentage = service.getPercentage();
    }
    public OrderItem(int quantity, Order order, Product product, User user, int lastPrice) {
        this.orderId = order.getId();
        this.serviceCenter = serviceCenter;
        this.product = product;
        this.doneUser = user;
        this.quantity = quantity;
        this.lastPrice = lastPrice;
        this.soldPrice = product.getPrice();
    }
}
