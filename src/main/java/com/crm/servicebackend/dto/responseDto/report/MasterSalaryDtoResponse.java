package com.crm.servicebackend.dto.responseDto.report;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class MasterSalaryDtoResponse {
    private Long orderId;
    private Long serviceId;
    private String serviceName;
    private int soldPrice;
    private int quantity;
    private int percentage;
    private int masterIncome;

    public MasterSalaryDtoResponse(HashMap<String, Object> values) {
        this.orderId = (Long) values.get("orderId");
        this.serviceId = (Long) values.get("serviceId");
        this.serviceName = (String) values.get("serviceName");
        this.soldPrice = (int) values.get("soldPrice");
        this.quantity = (int) values.get("quantity");
        this.percentage = (int) values.get("percentage");
        this.masterIncome = (int) values.get("masterIncome");
    }

}
