package com.crm.servicebackend.dto.responseDto.orderItem;

import com.crm.servicebackend.dto.responseDto.product.ProductItemDtoResponse;
import com.crm.servicebackend.dto.responseDto.service.ServiceItemDtoResponse;
import com.crm.servicebackend.dto.responseDto.user.UserItemDtoResponse;
import lombok.Data;

@Data
public class OrderItemOrderDtoResponse {
    private Long id;
    private Long orderId;
    private ServiceItemDtoResponse service;
    private ProductItemDtoResponse product;
    private UserItemDtoResponse doneUser;
    private int soldPrice;
    private int quantity;
}
