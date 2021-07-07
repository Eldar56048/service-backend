package com.crm.servicebackend.repository;

import com.crm.servicebackend.dto.responseDto.product.ProductDtoResponse;
import com.crm.servicebackend.dto.responseDto.product.ProductForSelectDtoResponse;
import com.crm.servicebackend.dto.responseDto.statistics.*;
import com.crm.servicebackend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select new com.crm.servicebackend.dto.responseDto.product.ProductDtoResponse(p.id, p.name, p.description, p.price, s.quantity) from Product p inner join Storage  s on s.product.id = p.id where p.serviceCenter.id = :serviceCenterId")
    Page<ProductDtoResponse> findAll(Long serviceCenterId, Pageable pageable);
    Product findByIdAndServiceCenterId(Long productId, Long serviceCenterId);
    Boolean existsByIdAndServiceCenterId(Long productId, Long serviceCenterId);
    Boolean existsByNameAndServiceCenterId(String name, Long serviceCenterId);
    Boolean existsByNameAndIdNotLikeAndServiceCenterId(String name, Long productId, Long serviceCenterId);
    @Query("select new com.crm.servicebackend.dto.responseDto.product.ProductDtoResponse(p.id, p.name, p.description, p.price, s.quantity) from Product p inner join Storage s on s.product.id = p.id where p.serviceCenter.id = :serviceCenterId AND (p.name like %:title% OR p.description like %:title% OR concat(p.id, '') like %:title% OR concat(p.price, '') like %:title% )")
    Page<ProductDtoResponse> findAllAndFilter(Long serviceCenterId, String title, Pageable pageable);
    @Query("SELECT new com.crm.servicebackend.dto.responseDto.product.ProductForSelectDtoResponse(p.id,p.name ,s.quantity) from Product p INNER join Storage s on s.product.id=p.id where p.serviceCenter.id = :serviceCenterId")
    List<ProductForSelectDtoResponse> getProductsForSelect(Long serviceCenterId);
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
            " LEFT JOIN (select * from order_items where order_items.product_id= ?1 and order_items.service_center_id = ?2) itm on ordItm.items_id = itm.id\n" +
            " where meses.month <= month(now()) \n" +
            " GROUP BY meses.month\n")
    List<MonthlySale> getMonthlySold(long id, long serviceCenterId);

    @Query(nativeQuery = true, value = "select p.id, p.name as name, sum(itm.quantity) as count from orders o" +
            " INNER JOIN orders_items ordItm on ordItm.order_id = o.id" +
            " inner JOIN order_items itm on itm.id = ordItm.items_id" +
            " Inner Join products p on itm.product_id = p.id" +
            " where o.service_center_id=?1"+
            " GROUP by p.id" +
            " order by count desc" +
            " limit 1")
    public TopSaleItem getTopSaleProduct(Long serviceCenterId);

    @Query(nativeQuery = true, value = "select sum(itm.quantity) as count from orders o" +
            " INNER JOIN orders_items ordItm on ordItm.order_id = o.id" +
            " inner JOIN order_items itm on itm.id = ordItm.items_id" +
            " Inner Join products p on itm.product_id = p.id"+
            " where o.service_center_id = ?1"
    )
    public SoldItemCount getSoldProductCount(Long serviceCenterId);

    @Query(nativeQuery = true, value = "select p.id, p.name as name, sum(itm.quantity*(itm.sold_price-itm.last_price)) as profit from orders o INNER JOIN orders_items ordItm on ordItm.order_id = o.id inner JOIN order_items itm on itm.id = ordItm.items_id INNER JOIN products p on p.id = itm.product_id WHERE o.status = 2 and o.service_center_id=?1 group by p.id Order by profit desc LIMIT ?2")
    public List<TopProfitItem> getTopProfitProducts(Long serviceCenterId, Long limit);

    @Query(nativeQuery = true, value = "SELECT COALESCE(sum(o.quantity),0) as soldCount from order_items o where o.product_id = ?1 and o.service_center_id = ?2")
    Sold getProductSoldCount(long productId, long serviceCenterId);
}
