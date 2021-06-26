package com.crm.servicebackend.dto.responseDto.serviceCenter;

import lombok.Data;

@Data
public class ServiceCenterDtoResponse {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private Boolean isEnabled;
    private String comment;
}
