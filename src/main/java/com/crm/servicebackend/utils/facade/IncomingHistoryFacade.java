package com.crm.servicebackend.utils.facade;

import com.crm.servicebackend.dto.responseDto.incomingHistory.IncomingHistoryDtoResponse;
import com.crm.servicebackend.model.IncomingHistory;

public class IncomingHistoryFacade {
    public static IncomingHistoryDtoResponse modelToDtoResponse(IncomingHistory model) {
        IncomingHistoryDtoResponse dto = new IncomingHistoryDtoResponse();
        if (model.getId() != null)
            dto.setId(model.getId());
        if (model.getProvider() != null) {
            dto.setProvider(model.getProvider().getName());
            dto.setProviderId(model.getProvider().getId());
        }
        if (model.getDate() != null)
            dto.setDate(model.getDate());
        dto.setQuantity(model.getQuantity());
        dto.setIncomePrice(model.getPrice());
        return dto;
    }

}
