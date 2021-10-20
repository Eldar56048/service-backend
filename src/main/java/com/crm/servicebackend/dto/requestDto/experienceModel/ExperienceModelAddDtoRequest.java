package com.crm.servicebackend.dto.requestDto.experienceModel;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.crm.servicebackend.constant.model.experienceModel.ExperienceModelValidationConstants.*;

@Data
public class ExperienceModelAddDtoRequest {

    @NotBlank(message = FIELD_NAME_REQUIRED_MESSAGE)
    @Length(message = FIELD_NAME_LENGTH_MESSAGE, min = 1)
    private String name;

    @NotNull(message = FIELD_COEFFICIENT_REQUIRED_MESSAGE)
    @Min(value = 0,message = FIELD_COEFFICIENT_MIN_VALUE_MESSAGE)
    @Max(value = 100, message = FIELD_COEFFICIENT_MAX_VALUE_MESSAGE)
    private int coefficient;
}
