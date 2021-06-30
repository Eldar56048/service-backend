package com.crm.servicebackend.dto.responseDto.order;

import com.crm.servicebackend.model.Status;
import lombok.Data;

import java.util.Date;

@Data
public class OrderForListDtoResponse {
    private Long id;
    private String name;
    private String number;
    private Status status;
    private String model;
    private String type;
    private Date accepted;
    private Long clientId;
}
