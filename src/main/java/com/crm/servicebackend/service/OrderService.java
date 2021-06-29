package com.crm.servicebackend.service;

import com.crm.servicebackend.model.Order;
import com.crm.servicebackend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository repository;

    @Autowired
    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public Order get(Long orderId, Long serviceCenterId) {
        return repository.findByIdAndServiceCenterId(orderId, serviceCenterId);
    }

    public Order save(Order order) {
        return repository.save(order);
    }
}
