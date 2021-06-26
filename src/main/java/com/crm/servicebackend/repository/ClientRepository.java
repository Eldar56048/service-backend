package com.crm.servicebackend.repository;

import com.crm.servicebackend.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    /*Page<Client> findAllByServiceCenterId(Long serviceCenterId, Pageable pageable);
    @Query("select c from Client c where c.serviceCenter.id = :id AND (c.clientName like %:title% OR c.clientSurname like %:title% OR  c.phoneNumber like %:title%)")
    Page<Client> findAllByServiceCenterIdAndFilter(Long id, String title, Pageable pageable);*/
}
