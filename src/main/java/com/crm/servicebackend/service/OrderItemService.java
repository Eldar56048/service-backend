package com.crm.servicebackend.service;

import com.crm.servicebackend.model.OrderItem;
import com.crm.servicebackend.model.Product;
import com.crm.servicebackend.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    private final OrderItemRepository repository;

    public OrderItemService(OrderItemRepository repository) {
        this.repository = repository;
    }

    public OrderItem get(Long id, Long serviceCenterId) {
        return this.repository.findByIdAndServiceCenterId(id, serviceCenterId);
    }

    public List<OrderItem> getAllByProductAndServiceCenterId(Product product, Long serviceCenterId) {
        return repository.getAllByProductAndServiceCenterId(product, serviceCenterId);
    }
    public List<OrderItem> getAllByServiceAndServiceCenterId(com.crm.servicebackend.model.Service service, Long serviceCenterId) {
        return repository.getAllByServiceAndServiceCenterId(service, serviceCenterId);
    }
}
