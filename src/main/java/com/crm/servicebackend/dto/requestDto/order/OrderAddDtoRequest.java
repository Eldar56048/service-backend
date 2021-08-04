package com.crm.servicebackend.dto.requestDto.order;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import static com.crm.servicebackend.constant.model.order.OrderValidationConstants.*;

@Data
public class OrderAddDtoRequest {

    @NotBlank(message = FIELD_CLIENT_NUMBER_REQUIRED_MESSAGE)
    @Pattern(regexp = "[8][0-9]{10}", message = FIELD_CLIENT_NUMBER_FORMAT_MESSAGE)
    private String client_number;

    @NotBlank(message = FIELD_CLIENT_ID_REQUIRED_MESSAGE)
    @Length(message = FIELD_CLIENT_ID_LENGTH_MESSAGE, min = 1)
    private String clientId;

    @NotBlank(message = FIELD_PROBLEM_REQUIRED_MESSAGE)
    @Length(message = FIELD_PROBLEM_LENGTH_MESSAGE, min = 1)
    private String problem;

    @NotBlank(message = FIELD_MODEL_TYPE_REQUIRED_MESSAGE)
    @Length(message = FIELD_MODEL_TYPE_LENGTH_MESSAGE, min = 1)
    private String modelType;

    @NotNull(message = FIELD_TYPE_ID_REQUIRED_MESSAGE)
    @Positive(message = FIELD_TYPE_ID_CANNOT_BE_NEGATIVE_MESSAGE)
    private Long type_id;

    @NotNull(message = FIELD_MODEL_ID_REQUIRED_MESSAGE)
    @Positive(message = FIELD_MODEL_ID_CANNOT_BE_NEGATIVE_MESSAGE)
    private Long model_id;

    @NotNull(message = FIELD_DISCOUNT_ID_REQUIRED_MESSAGE)
    @Positive(message = FIELD_DISCOUNT_ID_CANNOT_BE_NEGATIVE_MESSAGE)
    private Long discount_id;

}
