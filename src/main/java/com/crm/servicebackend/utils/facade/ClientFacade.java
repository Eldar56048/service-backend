package com.crm.servicebackend.utils.facade;

import com.crm.servicebackend.dto.requestDto.client.ClientAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.client.ClientUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.client.ClientDtoResponse;
import com.crm.servicebackend.model.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientFacade {

    public static ClientDtoResponse modelToDtoResponse(Client model) {
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
        return dto;
    }

    public static List<ClientDtoResponse> modelListToDtoResponseList(List<Client> modelList) {
        List<ClientDtoResponse> dtoList = new ArrayList<>();
        for(Client model:modelList) {
            dtoList.add(modelToDtoResponse(model));
        }
        return dtoList;
    }

    public static Client addDtoToModel(ClientAddDtoRequest dto) {
        Client model = new Client();
        model.setName(dto.getName());
        model.setSurname(dto.getSurname());
        model.setPhoneNumber(dto.getPhoneNumber());
        return model;
    }

    public static Client updateDtoToModel(Client model, ClientUpdateDtoRequest dto) {
        model.setName(dto.getName());
        model.setSurname(dto.getSurname());
        model.setPhoneNumber(dto.getPhoneNumber());
        return model;
    }
}
