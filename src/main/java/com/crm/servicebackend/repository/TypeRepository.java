package com.crm.servicebackend.repository;

import com.crm.servicebackend.model.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {
    Page<Type> findAllByServiceCenterId(Long serviceCenterId, Pageable pageable);
    Type findByIdAndServiceCenterId(Long typeId, Long serviceCenterId);
    Boolean existsByIdAndServiceCenterId(Long typeId, Long serviceCenterId);
    Boolean existsByNameAndServiceCenterId(String name, Long serviceCenterId);
    Boolean existsByNameAndIdNotLikeAndServiceCenterId(String name, Long typeId, Long serviceCenterId);
    @Query("select t from Type t where t.serviceCenter.id =:serviceCenterId AND (t.name like %:title% or concat(t.id, '') like %:title%)")
    Page<Type> findAndFilter(Long serviceCenterId, String title, Pageable pageable);
}
