package com.crm.servicebackend.service;

import com.crm.servicebackend.model.Product;
import com.crm.servicebackend.model.ReceivingHistory;
import com.crm.servicebackend.repository.ReceivingHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceivingHistoryService {
    private final ReceivingHistoryRepository repository;

    @Autowired
    public ReceivingHistoryService(ReceivingHistoryRepository repository) {
        this.repository = repository;
    }

    public void delete(long id){
        repository.deleteById(id);
    }

    public void deleteAllByOrderItemProductAndServiceCenterId(Product product, Long serviceCenterId) {
        repository.deleteAllByOrderItemProductAndServiceCenterId(product, serviceCenterId);
    }
    public ReceivingHistory findByOrderItemId(Long orderItemId) {
        return repository.findByOrderItemId(orderItemId);
    }
    public ReceivingHistory save(ReceivingHistory receivingHistory) {
        return  repository.save(receivingHistory);
    }
}
