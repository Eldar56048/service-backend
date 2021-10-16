package com.crm.servicebackend.dto.responseDto.report;

import com.crm.servicebackend.model.enums.TypesOfPayments;

import java.util.Date;

public interface GivenOrder {
    Long getOrderId();
    Date getAcceptedDate();
    Date getGaveDate();
    String getGiveUser();
    Long getGiveUserId();
    double getOrderSum();
    TypesOfPayments getPaymentType();
    double getNetProfit();
}
