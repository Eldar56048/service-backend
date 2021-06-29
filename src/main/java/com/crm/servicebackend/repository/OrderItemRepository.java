package com.crm.servicebackend.repository;

import com.crm.servicebackend.model.OrderItem;
import com.crm.servicebackend.model.Product;
import com.crm.servicebackend.model.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Page<OrderItem> findAllByServiceCenterId(Long serviceCenterId, Pageable pageable);
    OrderItem findByIdAndServiceCenterId(Long itemId, Long serviceCenterId);
    Boolean existsByIdAndServiceCenterId(Long itemId, Long serviceCenterId);
    List<OrderItem> getAllByProductAndServiceCenterId(Product product, Long serviceCenterId);
    List<OrderItem> getAllByServiceAndServiceCenterId(Service service, Long serviceCenterId);
}
