package com.crm.servicebackend.utils.facade;

import com.crm.servicebackend.dto.requestDto.service.ServiceAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.service.ServiceUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.service.ServiceDtoResponse;
import com.crm.servicebackend.dto.responseDto.service.ServiceItemDtoResponse;
import com.crm.servicebackend.model.Service;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.model.enums.Role;

public class ServiceFacade {
    public static ServiceDtoResponse modelToDtoResponse(Service model) {
        ServiceDtoResponse dto = new ServiceDtoResponse();
        if (model.getId() != null)
            dto.setId(model.getId());
        if (model.getName() != null)
            dto.setName(model.getName());
        if (model.getDescription() != null)
            dto.setDescription(model.getDescription());
        dto.setPercentage(model.getPercentage());
        dto.setPrice(model.getPrice());
        return dto;
    }

    public static Service addDtoToModel(ServiceAddDtoRequest dto, User user) {
        Service model = new Service();
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        if (user.getRoles().contains(Role.ADMIN))
            model.setPercentage(dto.getPercentage());
        model.setPrice(dto.getPrice());
        return model;
    }

    public static Service updateDtoToModel(Service model, ServiceUpdateDtoRequest dto, User user) {
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        if (user.getRoles().contains(Role.ADMIN))
            model.setPercentage(dto.getPercentage());
        model.setPrice(dto.getPrice());
        return model;
    }

    public static ServiceItemDtoResponse modelToItemDto(Service model) {
        ServiceItemDtoResponse dto = new ServiceItemDtoResponse();
        if (model.getId() != null)
            dto.setId(model.getId());
        if (model.getName() != null)
            dto.setName(model.getName());
        return dto;
    }
}
