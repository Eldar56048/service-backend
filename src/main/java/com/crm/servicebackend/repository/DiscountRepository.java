package com.crm.servicebackend.repository;

import com.crm.servicebackend.model.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Page<Discount> findAllByServiceCenterId(Long serviceCenterId, Pageable pageable);
    Discount findByIdAndServiceCenterId(Long discountId, Long serviceCenterId);
    Discount findByDiscountNameAndServiceCenterId(String discountName, Long serviceCenterId);
    Boolean existsByIdAndServiceCenterId(Long discountId, Long serviceCenterId);
    Boolean existsByDiscountNameAndServiceCenterId(String discountName, Long serviceCenterId);
    Boolean existsByDiscountNameAndIdNotLikeAndServiceCenterId(String discountName, Long discountId, Long serviceCenterId);
    Boolean existsByPercentageAndServiceCenterId(int percentage, Long serviceCenterId);
    Boolean existsByPercentageAndIdNotLikeAndServiceCenterId(int percentage,Long discountId, Long serviceCenterId);
    @Query("select d from Discount d where d.serviceCenter.id =:serviceCenterId AND (concat(d.id,'') like %:title% OR d.discountName like %:title% OR concat(d.percentage, '') like %:title% )")
    Page<Discount> findAndFilter(Long serviceCenterId, String title, Pageable pageable);
    List<Discount> getAllByServiceCenterId(Long serviceCenterId);
}
