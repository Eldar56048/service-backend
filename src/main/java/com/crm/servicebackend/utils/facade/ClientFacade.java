package com.crm.servicebackend.utils.facade;

import com.crm.servicebackend.dto.requestDto.client.ClientAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.client.ClientUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.client.ClientDtoResponse;
import com.crm.servicebackend.model.Client;

public class ClientFacade {

    private static ClientDtoResponse modelToDtoResponse(Client model) {
        ClientDtoResponse dto = new ClientDtoResponse();
        if (model.getId() != null)
            dto.setId(model.getId());
        if (model.getName() != null)
            dto.setName(model.getName());
        if (model.getSurname() != null)
            dto.setSurname(model.getSurname());
        if (model.getPhoneNumber() != null)
            dto.setPhoneNumber(model.getPhoneNumber());
        if (model.getDiscount() != null)
            dto.setDiscount(DiscountFacade.modelToDtoResponse(model.getDiscount()));
        if (model.getServiceCenter() != null)
            dto.setServiceCenterId(model.getServiceCenter().getId());
        return dto;
    }

    private static Client addDtoToModel(ClientAddDtoRequest dto) {
        Client model = new Client();
        model.setName(dto.getName());
        model.setSurname(dto.getSurname());
        model.setPhoneNumber(dto.getPhoneNumber());
        return model;
    }

    private static Client updateDtoToModel(Client model, ClientUpdateDtoRequest dto) {
        model.setName(dto.getName());
        model.setSurname(dto.getSurname());
        model.setPhoneNumber(dto.getPhoneNumber());
        return model;
    }
}
