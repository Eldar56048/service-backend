package com.crm.servicebackend.repository;

import com.crm.servicebackend.model.Product;
import com.crm.servicebackend.model.ReceivingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ReceivingHistoryRepository extends JpaRepository<ReceivingHistory, Long> {
    @Transactional
    void deleteAllByOrderItemProductAndServiceCenterId(Product product, Long serviceCenterId);
}
