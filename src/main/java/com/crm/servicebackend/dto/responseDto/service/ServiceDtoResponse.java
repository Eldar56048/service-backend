package com.crm.servicebackend.dto.responseDto.service;

import lombok.Data;

@Data
public class ServiceDtoResponse {
    private Long id;
    private String name;
    private String description;
    private int percentage;
    private int price;
}
