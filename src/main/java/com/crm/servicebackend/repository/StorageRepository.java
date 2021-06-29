package com.crm.servicebackend.repository;

import com.crm.servicebackend.model.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
    Storage findByProductIdAndServiceCenterId(Long productId, Long serviceCenterId);
    Boolean existsByProductIdAndServiceCenterId(Long productId, Long serviceCenterId);
}
