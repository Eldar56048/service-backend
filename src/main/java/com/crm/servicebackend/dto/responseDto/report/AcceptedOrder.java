package com.crm.servicebackend.dto.responseDto.report;

import com.crm.servicebackend.model.enums.Status;

import java.util.Date;

public interface AcceptedOrder {
    Long getOrderId();
    Date getAcceptedDate();
    Long getClientId();
    String getClientName();
    String getClientNumber();
    Status getStatus();
    String getAcceptedUser();
    Long getAcceptedUserId();
}
