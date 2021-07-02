package com.crm.servicebackend.utils.facade;

import com.crm.servicebackend.dto.requestDto.type.TypeAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.type.TypeUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.type.TypeDtoResponse;
import com.crm.servicebackend.model.Type;

import java.util.ArrayList;
import java.util.List;

public class TypeFacade {
    public static TypeDtoResponse modelToDtoResponse(Type model) {
        TypeDtoResponse dto = new TypeDtoResponse();
        if (model.getId() != null)
            dto.setId(model.getId());
        if (model.getName() != null)
            dto.setName(model.getName());
        return dto;
    }

    public static List<TypeDtoResponse> modelListToDtoResponseList(List<Type> modelList) {
        List<TypeDtoResponse> dtoList = new ArrayList<>();
        for (Type model:modelList) {
            dtoList.add(modelToDtoResponse(model));
        }
        return dtoList;
    }

    public static Type addDtoToModel(TypeAddDtoRequest dto) {
        Type model = new Type();
        model.setName(dto.getName());
        return model;
    }

    public static Type updateDtoToModel(Type model, TypeUpdateDtoRequest dto) {
        model.setName(dto.getName());
        return model;
    }
}
