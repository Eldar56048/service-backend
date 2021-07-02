package com.crm.servicebackend.repository;

import com.crm.servicebackend.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Page<Client> findAllByServiceCenterId(Long serviceCenterId, Pageable pageable);
    Client findByIdAndServiceCenterId(Long discountId, Long serviceCenterId);
    Boolean existsByIdAndServiceCenterId(Long discountId, Long serviceCenterId);
    @Query("select c from Client c where c.serviceCenter.id =:serviceCenterId AND (c.name like %:title% OR c.surname like %:title% OR c.phoneNumber like %:title% OR concat(c.id, '') like %:title% or c.discount.discountName like %:title%)")
    Page<Client> findAndFilter(Long serviceCenterId, String title, Pageable pageable);
    List<Client> getAllByServiceCenterId(Long serviceCenterId);
}
