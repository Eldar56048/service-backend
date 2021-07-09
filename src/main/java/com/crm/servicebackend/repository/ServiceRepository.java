package com.crm.servicebackend.repository;

import com.crm.servicebackend.dto.responseDto.service.ServiceForSelectDtoResponse;
import com.crm.servicebackend.dto.responseDto.statistics.*;
import com.crm.servicebackend.model.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Page<Service> findAllByServiceCenterId(Long serviceCenterId, Pageable pageable);
    Service findByIdAndServiceCenterId(Long serviceId, Long serviceCenterId);
    Boolean existsByIdAndServiceCenterId(Long serviceId, Long serviceCenterId);
    Boolean existsByNameAndServiceCenterId(String name, Long serviceCenterId);
    Boolean existsByNameAndIdNotLikeAndServiceCenterId(String name, Long serviceId, Long serviceCenterId);
    @Query("select s from Service s where s.serviceCenter.id =:serviceCenterId AND (s.name like %:title% OR s.description like %:title% OR concat(s.id,'') like %:title% or concat(s.price, '') like %:title% or concat(s.percentage,'') like %:title%)")
    Page<Service> findAndFilter(Long serviceCenterId, String title, Pageable pageable);
    @Query("SELECT new com.crm.servicebackend.dto.responseDto.service.ServiceForSelectDtoResponse(s.id,s.name) from Service s where s.serviceCenter.id = :serviceCenterId")
    List<ServiceForSelectDtoResponse> getServicesForSelect(Long serviceCenterId);
    @Query(nativeQuery = true,value = "SELECT meses.month as month , ifnull(sum(itm.quantity),0) as count \n" +
            "FROM \n" +
            "( SELECT 1 AS MONTH UNION\n" +
            " SELECT 2 AS MONTH UNION\n" +
            " SELECT 3 AS MONTH UNION\n" +
            " SELECT 4 AS MONTH UNION\n" +
            " SELECT 5 AS MONTH UNION\n" +
            " SELECT 6 AS MONTH UNION\n" +
            " SELECT 7 AS MONTH UNION\n" +
            " SELECT 8 AS MONTH UNION\n" +
            " SELECT 9 AS MONTH UNION\n" +
            " SELECT 10 AS MONTH UNION\n" +
            " SELECT 11 AS MONTH UNION\n" +
            " SELECT 12 AS MONTH ) as meses\n" +
            " LEFT JOIN (select*from orders where Year(orders.gave_date)=Year(now()) and orders.service_center_id = ?2) o on meses.month = month(o.gave_date)\n" +
            " LEFT JOIN orders_items ordItm on o.id = ordItm.order_id\n" +
            " LEFT JOIN (select * from order_items where order_items.service_id= ?1 and order_items.service_center_id = ?2) itm on ordItm.items_id = itm.id\n" +
            " where meses.month <= month(now()) \n" +
            " GROUP BY meses.month\n")
    List<MonthlySale> getMonthlySold(long serviceId, long serviceCenterId);

    @Query(nativeQuery = true, value = "select s.id, s.name as name, sum(itm.quantity) as count from orders o" +
            " INNER JOIN orders_items ordItm on ordItm.order_id = o.id" +
            " inner JOIN order_items itm on itm.id = ordItm.items_id" +
            " Inner Join services s on itm.service_id = s.id" +
            " where o.service_center_id= ?1"+
            " GROUP by s.id order by count desc" +
            " limit 1")
    public TopSaleItem getTopSaleService(Long serviceCenterId);

    @Query(nativeQuery = true, value = "select sum(itm.quantity) as count from orders o" +
            " INNER JOIN orders_items ordItm on ordItm.order_id = o.id" +
            " inner JOIN order_items itm on itm.id = ordItm.items_id" +
            " Inner Join services s on itm.service_id = s.id"+
            " where o.service_center_id = ?1"
    )
    public SoldItemCount getSoldServiceCount(Long serviceCenterId);

    @Query(nativeQuery = true, value = "select s.id, s.name as name, sum(if(itm.product_id, (itm.quantity*(itm.sold_price-itm.last_price)), ((itm.quantity)*((itm.sold_price*(if(o.discount_percent>0, (100-o.discount_percent)/100, 1)) - ((itm.sold_price*((if(o.discount_percent>0, (100-o.discount_percent)/100,1)) * (itm.service_percentage)/100 * (ex.coefficient)/100 ))))) ))) as profit from orders o INNER JOIN orders_items ordItm on ordItm.order_id = o.id inner JOIN order_items itm on itm.id = ordItm.items_id inner JOIN experience_models ex on ex.id = COALESCE(itm.user_experience_id, 103) INNER JOIN services s on s.id = itm.service_id WHERE o.status = 2 and o.service_center_id = ?1 GROUP BY s.id Order by profit desc LIMIT ?2")
    public List<TopProfitItem> getTopProfitServices(Long serviceCenterId, int limit);

    @Query(nativeQuery = true, value = "SELECT COALESCE(sum(o.quantity),0) as soldCount from order_items o where o.service_id = ?1 and o.service_center_id = ?2")
    Sold getServiceSoldCount(long serviceId, long serviceCenterId);
}
