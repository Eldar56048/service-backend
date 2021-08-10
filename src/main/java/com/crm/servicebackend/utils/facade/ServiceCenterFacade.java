package com.crm.servicebackend.utils.facade;

import com.crm.servicebackend.dto.requestDto.serviceCenter.ServiceCenterAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.serviceCenter.ServiceCenterUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.serviceCenter.ServiceCenterDtoResponse;
import com.crm.servicebackend.model.ServiceCenter;

public class ServiceCenterFacade {

    public static ServiceCenterDtoResponse modelToDtoResponse(ServiceCenter model) {
        ServiceCenterDtoResponse dto = new ServiceCenterDtoResponse();
        if (model.getId() != null)
            dto.setId(model.getId());
        if (model.getName() != null)
            dto.setName(model.getName());
        if (model.getAddress() != null)
            dto.setAddress(model.getAddress());
        if (model.getPhoneNumber() != null)
            dto.setPhoneNumber(model.getPhoneNumber());
        if (model.getComment() != null)
            dto.setComment(model.getComment());
        dto.setIsEnabled(model.isEnabled());
        return dto;
    }

    public static ServiceCenter addDtoToModel(ServiceCenterAddDtoRequest dto) {
        ServiceCenter serviceCenter = new ServiceCenter();
        serviceCenter.setName(dto.getName());
        serviceCenter.setAddress(dto.getAddress());
        serviceCenter.setPhoneNumber(dto.getPhoneNumber());
        serviceCenter.setEmail(dto.getEmail());
        serviceCenter.setComment(dto.getComment());
        return serviceCenter;
    }

    public static ServiceCenter updateDtoToModel(ServiceCenter model, ServiceCenterUpdateDtoRequest dto){
        model.setName(dto.getName());
        model.setAddress(dto.getAddress());
        model.setPhoneNumber(dto.getPhoneNumber());
        model.setEnabled(dto.getIsEnabled());
        model.setComment(dto.getComment());
        return model;
    }
}
