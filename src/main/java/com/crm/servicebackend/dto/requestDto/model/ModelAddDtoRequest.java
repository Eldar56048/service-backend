package com.crm.servicebackend.dto.requestDto.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

import static com.crm.servicebackend.constant.model.model.ModelValidationConstants.FIELD_NAME_LENGTH_MESSAGE;
import static com.crm.servicebackend.constant.model.model.ModelValidationConstants.FIELD_NAME_REQUIRED_MESSAGE;

@Data
public class ModelAddDtoRequest {

    @NotBlank(message = FIELD_NAME_REQUIRED_MESSAGE)
    @Length(message = FIELD_NAME_LENGTH_MESSAGE, min = 1)
    private String name;

}
