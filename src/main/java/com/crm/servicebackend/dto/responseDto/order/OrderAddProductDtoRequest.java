package com.crm.servicebackend.dto.responseDto.order;

import lombok.Data;

@Data
public class OrderAddProductDtoRequest {
    private Long product_id;
    private int quantity;
}

