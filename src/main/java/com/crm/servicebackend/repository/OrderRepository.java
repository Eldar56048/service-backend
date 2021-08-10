package com.crm.servicebackend.repository;

import com.crm.servicebackend.dto.responseDto.statistics.Count;
import com.crm.servicebackend.dto.responseDto.statistics.NetProfit;
import com.crm.servicebackend.model.Order;
import com.crm.servicebackend.model.enums.Status;
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
    Page<Order> findAllByServiceCenterIdAndClientId(Long serviceCenterId, Long clientId, Pageable pageable);
    @Query("select o from Order o where o.serviceCenter.id =:serviceCenterId AND o.client.id =:clientId AND ((o.clientName like %:title% OR o.phoneNumber like %:title% OR o.type.name like %:title% or o.model.name like %:title%  or concat(o.id, '') like %:title%))")
    Page<Order> findAllByServiceCenterIdAndClientAndFilter(Long serviceCenterId, Long clientId, String title, Pageable pageable);
    Page<Order> findAllByServiceCenterIdAndStatus(Long serviceCenterId, Status status, Pageable pageable);
    @Query("select o from Order o where o.serviceCenter.id =:serviceCenterId AND o.status=:status AND (o.client.name like %:title% OR o.client.surname like %:title% OR o.client.phoneNumber like %:title% OR o.clientName like %:title% OR o.phoneNumber like %:title% OR o.type.name like %:title% or o.model.name like %:title%  or concat(o.id, '') like %:title%)")
    Page<Order> findAllByServiceCenterIdAndStatusAndFilter(Long serviceCenterId, Status status, String title, Pageable pageable);
    @Query(nativeQuery = true, value = "SELECT count(o.id) as count FROM `orders` o where o.service_center_id=?1")
    public Count getOrdersCount(Long serviceCenterId);
    Boolean existsByIdAndServiceCenterId(Long orderId, Long serviceCenterId);
    @Query(nativeQuery = true, value = "select \n" +
            "sum(if(itm.product_id, \n" +
            "       (itm.quantity*(itm.sold_price-itm.last_price)),\n" +
            "       ((itm.quantity)*((itm.sold_price*(if(o.discount_percent>0, (100-o.discount_percent)/100, 1)) - ((itm.sold_price*((if(o.discount_percent>0, (100-o.discount_percent)/100,1)) * (itm.service_percentage)/100 * (ex.coefficient)/100 ))))) ))) as profit from orders o INNER JOIN orders_items ordItm on ordItm.order_id = o.id inner JOIN order_items itm on itm.id = ordItm.items_id inner JOIN experience_models ex on ex.id = COALESCE(itm.user_experience_id, 103) WHERE o.status = 2 and o.service_center_id =?1")
    public NetProfit getProfit(Long serviceCenterId);
    Boolean existsByIdAndToken(Long id, String token);
}
