package com.crm.servicebackend.repository;

import com.crm.servicebackend.dto.responseDto.report.*;
import com.crm.servicebackend.dto.responseDto.statistics.Count;
import com.crm.servicebackend.dto.responseDto.statistics.NetProfit;
import com.crm.servicebackend.model.Order;
import com.crm.servicebackend.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByIdAndServiceCenterId(Long id, Long serviceCenterId);
    Page<Order> findAllByServiceCenterId(Long serviceCenterId, Pageable pageable);
    @Query("select o from Order o where o.serviceCenter.id =:serviceCenterId AND (o.clientName like %:title% OR o.phoneNumber like %:title% OR o.client.phoneNumber like %:title% OR concat(o.client.surname, ' ', o.client.name) like %:title% OR concat(o.type.name, ' ', o.model.name) like %:title% or concat(o.id, '') like %:title%)")
    Page<Order> findAllByServiceCenterIdAndFilter(Long serviceCenterId, String title, Pageable pageable);
    Page<Order> findAllByServiceCenterIdAndClientId(Long serviceCenterId, Long clientId, Pageable pageable);
    @Query("select o from Order o where o.serviceCenter.id =:serviceCenterId AND o.client.id =:clientId AND ((o.clientName like %:title% OR o.phoneNumber like %:title% OR o.type.name like %:title% or o.model.name like %:title%  or concat(o.id, '') like %:title%))")
    Page<Order> findAllByServiceCenterIdAndClientAndFilter(Long serviceCenterId, Long clientId, String title, Pageable pageable);
    Page<Order> findAllByServiceCenterIdAndStatus(Long serviceCenterId, Status status, Pageable pageable);
    @Query("select o from Order o where o.serviceCenter.id =:serviceCenterId AND o.status=:status AND (concat(coalesce(o.client.surname, ''), ' ', coalesce(o.client.name, '') ) like %:title% OR coalesce(o.client.phoneNumber, '') like %:title% OR coalesce(o.clientName, '') like %:title% OR coalesce(o.phoneNumber, '') like %:title% OR o.type.name like %:title% or o.model.name like %:title%  or concat(o.id, '') like %:title%)")
    Page<Order> findAllByServiceCenterIdAndStatusAndFilter(Long serviceCenterId, Status status, String title, Pageable pageable);
    @Query(nativeQuery = true, value = "SELECT count(o.id) as count FROM `orders` o where o.service_center_id=?1")
    Count getOrdersCount(Long serviceCenterId);
    Boolean existsByIdAndServiceCenterId(Long orderId, Long serviceCenterId);
    @Query(nativeQuery = true, value = "select \n" +
            "sum(if(itm.product_id, \n" +
            "       (itm.quantity*(itm.sold_price-itm.last_price)),\n" +
            "       ((itm.quantity)*((itm.sold_price*(if(o.discount_percent>0, (100-o.discount_percent)/100, 1)) - ((itm.sold_price*((if(o.discount_percent>0, (100-o.discount_percent)/100,1)) * (itm.service_percentage)/100 * (ex.coefficient)/100 ))))) ))) as profit from orders o INNER JOIN orders_items ordItm on ordItm.order_id = o.id inner JOIN order_items itm on itm.id = ordItm.items_id inner JOIN experience_models ex on ex.id = COALESCE(itm.user_experience_id, 103) WHERE o.status = 2 and o.service_center_id =?1")
    NetProfit getProfit(Long serviceCenterId);

    @Query("SELECT o.id as orderId, service.id as serviceId, service.name as serviceName, itm.soldPrice as soldPrice, itm.quantity as quantity, o.discountPercent as percentage, ((itm.quantity * itm.soldPrice) * ((100 - o.discountPercent)/100) * (itm.servicePercentage/100) * (itm.userExperienceCoefficient/100)) as masterIncome from Order o " +
            "inner join OrderItem itm on itm.orderId = o.id " +
            "inner join Service service on service.id = itm.service.id " +
            "inner join User user on user.id = itm.doneUser.id " +
            "where o.status = com.crm.servicebackend.model.enums.Status.GIVEN and " +
            "itm.doneUser.id = :userId and " +
            "o.gaveDate >= :date1 and " +
            "o.gaveDate <= :date2 and " +
            "o.serviceCenter.id = :serviceCenterId")
    Page<MasterSalary> getMasterSalary(Long serviceCenterId, Long userId, Date date1, Date date2, Pageable pageable);

    @Query(nativeQuery = true, value = "select sum((itm.quantity * itm.sold_price) * ((100 - o.discount_percent)/100) * (itm.service_percentage/100) * (itm.user_experience_coefficient/100)) as masterIncome from orders o\n" +
            "inner join order_items itm on o.id = itm.order_id \n" +
            "inner join services service on itm.service_id = service.id\n" +
            "inner join users user on itm.done_user_id = user.id \n" +
            "where o.status = 2 and \n" +
            "itm.done_user_id = ?2 and \n" +
            "o.gave_date>= ?3 and\n" +
            "o.gave_date<= ?4 and \n" +
            "o.service_center_id = ?1 ")
    double getMasterSalarySum(Long serviceCenterId, Long userId, Date date1, Date date2);

    @Query(value = "SELECT o.id as orderId, o.acceptedDate as acceptedDate, COALESCE(c.id, null) as clientId, COALESCE(o.clientName, concat(c.surname, ' ', c.name)) as clientName, COALESCE(o.phoneNumber, c.phoneNumber) as clientNumber, o.status as status, concat(u.surname, ' ', u.name) as acceptedUser, u.id as acceptedUserId from Order o " +
            "inner join User u on u.id = o.acceptedUser.id " +
            "left join Client c on c.id = o.client.id " +
            "where o.serviceCenter.id = ?1 and o.acceptedDate>= ?2 and o.acceptedDate <= ?3")
    Page<AcceptedOrder> getAcceptedOrdersReport(Long serviceCenterId, Date date1, Date date2, Pageable pageable);

    @Query(value = "SELECT o.id as orderId, o.doneDate as doneDate, COALESCE(c.id, null) as clientId, COALESCE(o.clientName, concat(c.surname, ' ', c.name)) as clientName, COALESCE(o.phoneNumber, c.phoneNumber) as clientNumber, o.status as status, concat(u.surname, ' ', u.name) as doneUser, u.id as doneUserId from Order o " +
            "INNER join User u on u.id = o.doneUser.id " +
            "left join Client c on c.id = o.client.id " +
            "where o.serviceCenter.id = ?1 and o.doneDate >= ?2 and o.doneDate <= ?3 ")
    Page<DoneOrder> getDoneOrdersReport(Long serviceCenterId, Date date1, Date date2, Pageable pageable);

    /*@Query(nativeQuery = true, value = "select o.id as orderId, o.accepted_date as acceptedDate, o.gave_date as gaveDate, concat(u.surname, ' ', u.name) as giveUser, u.id as giveUserId, \n" +
            "sum((itm.sold_price * itm.quantity * (100-if(itm.service_id, o.discount_percent, 0))/100 )) as sum,\n" +
            "o.types_of_payments as paymentType, \n" +
            "sum( (if(itm.service_id, ( (itm.quantity * itm.sold_price) * ((100-o.discount_percent)/100) - (itm.quantity * itm.sold_price) * ((100-o.discount_percent)/100) * ((itm.service_percentage)/100) * ((itm.user_experience_coefficient)/100)) , (itm.sold_price * itm.quantity - itm.last_price * itm.quantity) ) ) ) as netProfit \n" +
            "from orders o \n" +
            "inner join order_items itm on o.id = itm.order_id \n" +
            "inner join users u on o.give_user_id = u.id \n" +
            "left join products product on product.id = itm.product_id \n" +
            "left join services service on service.id = itm.service_id \n" +
            "where o.status = 2 and o.gave_date>= ?2 and o.gave_date <= ?3 and o.service_center_id = ?1 GROUP by o.id ")
    Page<GivenOrder>  getGivenOrdersReport(Long serviceCenterId, Date date1, Date date2, Pageable pageable);
    */@Query(value = "select o.id as orderId, o.acceptedDate as acceptedDate, o.gaveDate as gaveDate, concat(u.surname, ' ', u.name) as giveUser, u.id as giveUserId, " +
            "sum(itm.soldPrice * itm.quantity * ((100- case when(itm.service.id != null) then o.discountPercent else 0 end)/100) ) as orderSum, " +
            "o.typesOfPayments as paymentType, " +
            "sum(case when(itm.service.id != null) then ( (itm.quantity * itm.soldPrice) * ((100-o.discountPercent)/100) - (itm.quantity * itm.soldPrice) * ((100-o.discountPercent)/100) * ((itm.servicePercentage)/100) * ((itm.userExperienceCoefficient)/100)) else (itm.soldPrice * itm.quantity - itm.lastPrice * itm.quantity) end ) as netProfit \n" +
            "from Order o " +
            "inner join OrderItem itm on o.id = itm.orderId " +
            "inner join User u on o.giveUser.id = u.id " +
            "left join Product product on product.id = itm.product.id " +
            "left join Service service on service.id = itm.service.id " +
            "where o.status = 2 and o.gaveDate>= ?2 and o.gaveDate <= ?3 and o.serviceCenter.id = ?1 GROUP by o.id ")
    Page<GivenOrder>  getGivenOrdersReport(Long serviceCenterId, Date date1, Date date2, Pageable pageable);

    @Query(nativeQuery = true, value = "select sum((itm.sold_price * itm.quantity * (100-if(itm.service_id, o.discount_percent, 0))/100 )) as orderSum,\n" +
            "sum( (if(itm.service_id, ( (itm.quantity * itm.sold_price) * ((100-o.discount_percent)/100) - (itm.quantity * itm.sold_price) * ((100-o.discount_percent)/100) * ((itm.service_percentage)/100) * ((itm.user_experience_coefficient)/100)) , (itm.sold_price * itm.quantity - itm.last_price * itm.quantity) ) ) ) as netProfitSum\n" +
            "from orders o\n" +
            "inner join order_items itm on o.id = itm.order_id\n" +
            "inner join users u on o.give_user_id = u.id \n" +
            "left join products product on product.id = itm.product_id\n" +
            "left join services service on service.id = itm.service_id\n" +
            "where o.status = 2 and \n" +
            "o.gave_date>= ?2 and\n" +
            "o.gave_date <= ?3 and \n" +
            "o.service_center_id = ?1")
    GivenOrderSum getGivenOrderSum(Long serviceCenterId, Date date1, Date date2);
    Boolean existsByIdAndToken(Long id, String token);
}
