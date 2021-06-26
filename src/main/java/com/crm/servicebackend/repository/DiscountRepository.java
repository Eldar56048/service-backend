package com.crm.servicebackend.repository;

import com.crm.servicebackend.model.Discount;
import com.crm.servicebackend.model.ServiceCenter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Page<Discount> findAllByServiceCenterId(Long serviceCenterId, Pageable pageable);
    Discount findByIdAndServiceCenterId(Long discountId, Long serviceCenterId);
    Boolean existsByIdAndServiceCenterId(Long discountId, Long serviceCenterId);
    Boolean existsByDiscountNameAndServiceCenterId(String discountName, Long serviceCenterId);
    Boolean existsByDiscountNameAndIdNotLikeAndServiceCenterId(String discountName, Long discountId, Long serviceCenterId);
    @Query("select d from Discount d where d.serviceCenter.id =:serviceCenterId AND (d.discountName like %:title%)")
    Page<Discount> findAndFilter(Long serviceCenterId, String title, Pageable pageable);
}
