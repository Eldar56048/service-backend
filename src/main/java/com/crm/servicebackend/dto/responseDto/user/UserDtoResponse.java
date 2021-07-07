package com.crm.servicebackend.dto.responseDto.user;

import com.crm.servicebackend.dto.responseDto.experienceModel.ExperienceModelDtoResponse;
import com.crm.servicebackend.model.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDtoResponse {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private Set<Role> roles;
    private String phoneNumber;
    private ExperienceModelDtoResponse experienceModel;
    private boolean enabled;
}
