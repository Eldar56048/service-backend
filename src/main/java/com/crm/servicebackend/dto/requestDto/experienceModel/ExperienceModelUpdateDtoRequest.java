package com.crm.servicebackend.dto.requestDto.experienceModel;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

import static com.crm.servicebackend.constant.model.experienceModel.ExperienceModelValidationConstants.*;

@Data
public class ExperienceModelUpdateDtoRequest {

    @NotNull(message = FIELD_ID_REQUIRED_MESSAGE)
    @Positive(message = FIELD_ID_CANNOT_BE_NEGATIVE_MESSAGE)
    private Long id;

    @NotBlank(message = FIELD_NAME_REQUIRED_MESSAGE)
    @Length(message = FIELD_NAME_LENGTH_MESSAGE, min = 1)
    private String name;

    @NotNull(message = FIELD_COEFFICIENT_REQUIRED_MESSAGE)
    @Min(value = 1,message = FIELD_COEFFICIENT_MIN_VALUE_MESSAGE)
    @Max(value = 100, message = FIELD_COEFFICIENT_MAX_VALUE_MESSAGE)
    private int coefficient;

}
