package com.crm.servicebackend.dto.responseDto.incomingHistory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomingHistoryDtoResponse {
    private Long id;
    private String provider;
    private long providerId;
    private int incomePrice;
    private int quantity;
    private Date date;
}
