package com.crm.servicebackend.dto.responseDto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDtoResponse {
    private Long id;
    private String name;
    private String description;
    private int price;
    private int storage;
}
