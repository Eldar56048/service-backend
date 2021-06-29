package com.crm.servicebackend.repository;

import com.crm.servicebackend.model.IncomingHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface IncomingHistoryRepository extends JpaRepository<IncomingHistory, Long> {
    Page<IncomingHistory> findAllByProductIdAndServiceCenterId(Long productId, Long serviceCenterId, Pageable pageable);
    IncomingHistory getFirstByProductIdAndServiceCenterIdOrderByDateDesc(Long productId, Long serviceCenterId);
    IncomingHistory getByIdAndServiceCenterId(Long id, Long serviceCenterId);
    Boolean existsByIdAndServiceCenter(Long id, Long serviceCenter);
    Boolean existsByProductIdAndServiceCenterId(Long productId, Long serviceCenter);
    @Transactional
    void deleteAllByProductId(long id);
    Page<IncomingHistory> findAllByServiceCenterId(Long serviceCenterId, Pageable pageable);
    @Query("select i from IncomingHistory i where i.product.id=:productId and i.serviceCenter.id =:serviceCenterId AND (i.provider.name like %:title% or concat(i.id, '') like %:title% or concat(i.price, '') like %:title% or concat(i.quantity, '') like %:title%)")
    Page<IncomingHistory> findAllByProductIdAndServiceCenterIdAndFilter(Long productId, Long serviceCenterId, String title, Pageable pageable);
    Boolean existsByIdAndProductIdAndServiceCenterId(Long historyId, Long productId, Long serviceCenterId);
}
