package com.crm.servicebackend.utils.facade;

import com.crm.servicebackend.dto.requestDto.providers.ProviderAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.providers.ProviderUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.provider.ProviderDtoResponse;
import com.crm.servicebackend.model.Provider;

import java.util.ArrayList;
import java.util.List;

public class ProviderFacade {
    public static ProviderDtoResponse modelToDtoResponse(Provider model) {
        ProviderDtoResponse dto = new ProviderDtoResponse();
        if (model.getId() != null)
            dto.setId(model.getId());
        if (model.getName() != null)
            dto.setName(model.getName());
        if (model.getAddress() != null)
            dto.setAddress(model.getAddress());
        if (model.getPhoneNumber() != null)
            dto.setPhoneNumber(model.getPhoneNumber());
        return dto;
    }

    public static List<ProviderDtoResponse> modelListToDtoResponseList(List<Provider> modelList) {
        List<ProviderDtoResponse> dtoList = new ArrayList<>();
        for (Provider model : modelList) {
            dtoList.add(modelToDtoResponse(model));
        }
        return dtoList;
    }

    public static Provider addDtoToModel(ProviderAddDtoRequest dto) {
        Provider model = new Provider();
        model.setName(dto.getName());
        model.setAddress(dto.getAddress());
        model.setPhoneNumber(dto.getPhoneNumber());
        return model;
    }

    public static Provider updateDtoToModel(Provider model, ProviderUpdateDtoRequest dto) {
        model.setName(dto.getName());
        model.setAddress(dto.getAddress());
        model.setPhoneNumber(dto.getPhoneNumber());
        return model;
    }
}
