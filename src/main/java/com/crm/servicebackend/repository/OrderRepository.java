package com.crm.servicebackend.repository;

import com.crm.servicebackend.dto.responseDto.statistics.Count;
import com.crm.servicebackend.model.Client;
import com.crm.servicebackend.model.Order;
import com.crm.servicebackend.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByIdAndServiceCenterId(Long id, Long serviceCenterId);
    Page<Order> findAllByServiceCenterId(Long serviceCenterId, Pageable pageable);
    @Query("select o from Order o where o.serviceCenter.id =:serviceCenterId AND (o.clientName like %:title% OR o.phoneNumber like %:title% OR o.type.name like %:title% or o.model.name like %:title%  or concat(o.id, '') like %:title%)")
    Page<Order> findAllByServiceCenterIdAndFilter(Long serviceCenterId, String title, Pageable pageable);
    Page<Order> findAllByServiceCenterIdAndClient(Long serviceCenterId, Client client, Pageable pageable);
    Page<Order> findAllByServiceCenterIdAndStatus(Long serviceCenterId, Status status, Pageable pageable);
    @Query(nativeQuery = true, value = "SELECT count(orders.id) as count FROM `orders` o where o.service_center_id=?1")
    public Count getOrdersCount(Long serviceCenterId);

}
