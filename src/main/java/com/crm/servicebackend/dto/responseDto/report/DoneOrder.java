package com.crm.servicebackend.dto.responseDto.report;

import com.crm.servicebackend.model.enums.Status;

import java.util.Date;

public interface DoneOrder {
    Long getOrderId();
    Date getDoneDate();
    Long getClientId();
    String getClientName();
    String getClientNumber();
    Status getStatus();
    String getDoneUser();
    Long getDoneUserId();
}
