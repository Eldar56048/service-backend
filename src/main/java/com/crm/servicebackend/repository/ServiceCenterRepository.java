package com.crm.servicebackend.repository;

import com.crm.servicebackend.model.ServiceCenter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCenterRepository extends JpaRepository<ServiceCenter, Long> {
    Boolean existsByName(String name);
    Boolean existsByNameAndIdNotLike(String name, Long id);
    @Query("select s from ServiceCenter s where (s.name like %:title% OR s.phoneNumber like %:title% OR s.address like %:title%)")
    Page<ServiceCenter> findAndFilter(String title, Pageable pageable);
}
