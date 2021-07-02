package com.crm.servicebackend.dto.responseDto.user;

import lombok.Data;

@Data
public class UserItemDtoResponse {
    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;
}
