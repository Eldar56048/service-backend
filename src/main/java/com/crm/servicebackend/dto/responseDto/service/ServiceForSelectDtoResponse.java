package com.crm.servicebackend.dto.responseDto.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceForSelectDtoResponse {
    private Long value;
    private String label;
}
