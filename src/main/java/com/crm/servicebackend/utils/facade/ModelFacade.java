package com.crm.servicebackend.utils.facade;

import com.crm.servicebackend.dto.requestDto.model.ModelAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.model.ModelUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.model.ModelDtoResponse;
import com.crm.servicebackend.model.Model;

import java.util.ArrayList;
import java.util.List;

public class ModelFacade {
    public static ModelDtoResponse modelToDtoResponse(Model model) {
        ModelDtoResponse dto = new ModelDtoResponse();
        if (model.getId() != null)
            dto.setId(model.getId());
        if (model.getName() != null)
            dto.setName(model.getName());
        return dto;
    }

    public static List<ModelDtoResponse> modelListToDtoResponseList(List<Model> modelList) {
        List<ModelDtoResponse> dtoList = new ArrayList<>();
        for(Model model:modelList) {
            dtoList.add(modelToDtoResponse(model));
        }
        return dtoList;
    }

    public static Model addDtoToModel(ModelAddDtoRequest dto) {
        Model model = new Model();
        model.setName(dto.getName());
        return model;
    }

    public static Model updateDtoToModel(Model model, ModelUpdateDtoRequest dto) {
        model.setName(dto.getName());
        return model;
    }
}
