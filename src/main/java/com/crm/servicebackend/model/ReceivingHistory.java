package com.crm.servicebackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "receiving_histories")
@Data
@NoArgsConstructor
public class ReceivingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false,unique = true)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "service_center_id")
    private ServiceCenter serviceCenter;
    @ManyToOne
    @JoinColumn(nullable = false, name = "order_item_id")
    private OrderItem orderItem;

    public ReceivingHistory(ServiceCenter serviceCenter, OrderItem orderItem) {
        this.serviceCenter = serviceCenter;
        this.orderItem = orderItem;
    }
}
