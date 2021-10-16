package com.crm.servicebackend.dto.responseDto.report;

public interface MasterSalary {
    long getOrderId();
    long getServiceId();
    String getServiceName();
    int getSoldPrice();
    int getQuantity();
    int getPercentage();
    int getMasterIncome();
}
