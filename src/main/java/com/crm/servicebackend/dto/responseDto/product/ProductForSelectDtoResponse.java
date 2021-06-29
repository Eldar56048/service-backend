package com.crm.servicebackend.dto.responseDto.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductForSelectDtoResponse {
    private Long value;
    private String name;
    private int storage;
}
