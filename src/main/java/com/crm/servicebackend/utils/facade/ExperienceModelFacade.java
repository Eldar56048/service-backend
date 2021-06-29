package com.crm.servicebackend.utils.facade;

import com.crm.servicebackend.dto.requestDto.experienceModel.ExperienceModelAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.experienceModel.ExperienceModelUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.experienceModel.ExperienceModelDtoResponse;
import com.crm.servicebackend.model.ExperienceModel;

public class ExperienceModelFacade {

    public static ExperienceModelDtoResponse modelToDtoResponse(ExperienceModel model){
        ExperienceModelDtoResponse dto = new ExperienceModelDtoResponse();
        if (model.getId() != null)
            dto.setId(model.getId());
        if (model.getName() != null)
            dto.setName(model.getName());
        dto.setCoefficient(model.getCoefficient());
        return dto;
    }

    public static ExperienceModel addDtoToModel(ExperienceModelAddDtoRequest dto) {
        ExperienceModel model = new ExperienceModel();
        model.setName(dto.getName());
        model.setCoefficient(dto.getCoefficient());
        return model;
    }

    public static ExperienceModel updateDtoToModel(ExperienceModel model, ExperienceModelUpdateDtoRequest dto) {
        model.setName(dto.getName());
        model.setCoefficient(dto.getCoefficient());
        return model;
    }

}
