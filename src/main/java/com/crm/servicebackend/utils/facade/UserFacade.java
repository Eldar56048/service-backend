package com.crm.servicebackend.utils.facade;

import com.crm.servicebackend.dto.requestDto.user.UserAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.user.UserAddOwnerDtoRequest;
import com.crm.servicebackend.dto.requestDto.user.UserUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.user.UserDtoResponse;
import com.crm.servicebackend.dto.responseDto.user.UserForSelectDtoResponse;
import com.crm.servicebackend.dto.responseDto.user.UserItemDtoResponse;
import com.crm.servicebackend.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserFacade {
    public static UserDtoResponse modelToDtoResponse(User model) {
        UserDtoResponse dto = new UserDtoResponse();
        if (model.getId() != null)
            dto.setId(model.getId());
        if (model.getName() != null)
            dto.setName(model.getName());
        if (model.getSurname() != null)
            dto.setSurname(model.getSurname());
        if (model.getUsername() != null)
            dto.setUsername(model.getUsername());
        if (model.getPhoneNumber() != null)
            dto.setPhoneNumber(model.getPhoneNumber());
        if (model.getRoles() != null)
            dto.setRoles(model.getRoles());
        if (model.getExperienceModel() != null)
            dto.setExperienceModel(ExperienceModelFacade.modelToDtoResponse(model.getExperienceModel()));
        dto.setEnabled(model.isEnabled());
        return dto;
    }

    public static User addDtoToModel(UserAddDtoRequest dto) {
        User model = new User();
        model.setName(dto.getName());
        model.setSurname(dto.getSurname());
        model.setPhoneNumber(dto.getPhoneNumber());
        model.setUsername(dto.getUsername());
        return model;
    }

    public static User updateDtoToModel(User model, UserUpdateDtoRequest dto) {
        model.setName(dto.getName());
        model.setSurname(dto.getSurname());
        model.setPhoneNumber(dto.getPhoneNumber());
        model.setUsername(dto.getUsername());
        model.setEnabled(dto.getEnabled());
        return model;
    }

    public static User addOwnerDtoToModel(UserAddOwnerDtoRequest dto) {
        User model = new User();
        model.setName(dto.getName());
        model.setSurname(dto.getSurname());
        model.setPhoneNumber(dto.getPhoneNumber());
        model.setUsername(dto.getUsername());
        return model;
    }

    public static UserForSelectDtoResponse modelToSelectDtoResponse(User model) {
        UserForSelectDtoResponse dto = new UserForSelectDtoResponse();
        if (model.getId() != null) {
            dto.setValue(model.getId());
        }
        if (model.getSurname() != null) {
            dto.setSurname(model.getSurname());
        }
        if (model.getName() != null) {
            dto.setName(model.getName());
        }
        return dto;
    }

    public static List<UserForSelectDtoResponse> modelListToSelectDtoResponseList(List<User> modelList) {
        List<UserForSelectDtoResponse> dtoList = new ArrayList<>();
        for(User model : modelList) {
            dtoList.add(modelToSelectDtoResponse(model));
        }
        return dtoList;
    }

    public static UserAddDtoRequest  AddOwnerDtoToAddDto(UserAddOwnerDtoRequest model) {
        UserAddDtoRequest dto = new UserAddDtoRequest();
        dto.setName(model.getName());
        dto.setSurname(model.getSurname());
        dto.setUsername(model.getUsername());
        dto.setPassword(model.getPassword());
        dto.setRoles(model.getRoles());
        dto.setPhoneNumber(model.getPhoneNumber());
        dto.setExperienceModelId(model.getExperienceModelId());
        return dto;
    }

    public static UserItemDtoResponse modelToItemDto(User model) {
        UserItemDtoResponse dto = new UserItemDtoResponse();
        if (model.getId() != null)
            dto.setId(model.getId());
        if (model.getName() != null)
            dto.setName(model.getName());
        if (model.getSurname() != null)
            dto.setSurname(model.getSurname());
        if (model.getPhoneNumber() != null)
            dto.setPhoneNumber(model.getPhoneNumber());
        return dto;
    }
}
