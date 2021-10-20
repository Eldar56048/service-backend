package com.crm.servicebackend.repository;

import com.crm.servicebackend.model.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    Page<Provider> findAllByServiceCenterId(Long serviceCenterId, Pageable pageable);
    Provider findByIdAndServiceCenterId(Long providerId, Long serviceCenterId);
    Provider findByNameAndServiceCenterId(String name, Long serviceCenterId);
    Boolean existsByIdAndServiceCenterId(Long providerId, Long serviceCenterId);
    Boolean existsByNameAndServiceCenterId(String name, Long serviceCenterId);
    Boolean existsByNameAndIdNotLikeAndServiceCenterId(String name, Long providerId, Long serviceCenterId);
    @Query("select p from Provider p where p.serviceCenter.id =:serviceCenterId AND (p.name like %:title% OR p.address like %:title% OR p.phoneNumber like %:title% or concat(p.id, '') like %:title%)")
    Page<Provider> findAndFilter(Long serviceCenterId, String title, Pageable pageable);
    List<Provider> getAllByServiceCenterId(Long serviceCenterId);
}
