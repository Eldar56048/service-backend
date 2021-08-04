package com.crm.servicebackend.dto.requestDto.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import static com.crm.servicebackend.constant.model.model.ModelValidationConstants.*;

@Data
public class ModelUpdateDtoRequest {

    @NotNull(message = FIELD_ID_REQUIRED_MESSAGE)
    @PositiveOrZero(message = FIELD_ID_CANNOT_BE_NEGATIVE_MESSAGE)
    private Long id;

    @NotBlank(message = FIELD_NAME_REQUIRED_MESSAGE)
    @Length(message = FIELD_NAME_LENGTH_MESSAGE, min = 1)
    private String name;

}
