package com.crm.servicebackend.dto.responseDto.client;

import com.crm.servicebackend.dto.responseDto.discount.DiscountDtoResponse;
import lombok.Data;

@Data
public class ClientDtoResponse {
    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private DiscountDtoResponse discount;
}
