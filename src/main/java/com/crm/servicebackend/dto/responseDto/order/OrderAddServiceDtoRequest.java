package com.crm.servicebackend.dto.responseDto.order;

import lombok.Data;

@Data
public class OrderAddServiceDtoRequest {
    private Long service_id;
    private int quantity;
}
