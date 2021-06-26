package com.crm.servicebackend.utils.facade;

import com.crm.servicebackend.dto.requestDto.discount.DiscountAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.discount.DiscountUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.discount.DiscountDtoResponse;
import com.crm.servicebackend.model.Discount;

public class DiscountFacade {
    public static DiscountDtoResponse modelToDtoResponse(Discount model) {
        DiscountDtoResponse dto = new DiscountDtoResponse();
        if (model.getId() != null)
            dto.setId(model.getId());
        if (model.getDiscountName() != null)
            dto.setDiscountName(model.getDiscountName());
        dto.setPercentage(model.getPercentage());
        if (model.getServiceCenter() != null ) {
            dto.setServiceCenterId(model.getServiceCenter().getId());
        }
        return dto;
    }

    public static Discount addDtoToModel(DiscountAddDtoRequest dto) {
        Discount model = new Discount();
        model.setDiscountName(dto.getDiscountName());
        model.setPercentage(dto.getPercentage());
        return model;
    }

    public static Discount updateDtoToModel(Discount model, DiscountUpdateDtoRequest dto) {
        model.setDiscountName(dto.getDiscountName());
        model.setPercentage(dto.getPercentage());
        return model;
    }


}
