package com.crm.servicebackend.dto.responseDto.provider;

import lombok.Data;

@Data
public class ProviderDtoResponse {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
}
