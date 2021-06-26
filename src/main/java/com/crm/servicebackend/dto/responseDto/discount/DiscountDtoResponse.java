package com.crm.servicebackend.dto.responseDto.discount;

import lombok.Data;

@Data
public class DiscountDtoResponse {
    private Long id;
    private String discountName;
    private int percentage;
    private Long serviceCenterId;
}
