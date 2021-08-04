package com.crm.servicebackend.dto.requestDto.client;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import static com.crm.servicebackend.constant.model.client.ClientValidationConstants.*;


@Data
public class ClientUpdateDtoRequest {

    @NotNull(message = FIELD_ID_REQUIRED_MESSAGE)
    @PositiveOrZero(message = FIELD_ID_CANNOT_BE_NEGATIVE_MESSAGE)
    private Long id;

    @NotBlank(message = FIELD_NAME_REQUIRED_MESSAGE)
    @Length(message = FIELD_NAME_LENGTH_MESSAGE, min = 1)
    private String name;

    @NotBlank(message = FIELD_SURNAME_REQUIRED_MESSAGE)
    @Length(message = FIELD_SURNAME_LENGTH_MESSAGE, min = 1)
    private String surname;

    @NotBlank(message = FIELD_PHONE_NUMBER_REQUIRED_MESSAGE)
    @Pattern(regexp = "[8][0-9]{10}", message = FIELD_PHONE_NUMBER_FORMAT_MESSAGE)
    private String phoneNumber;

    @NotNull(message = FIELD_DISCOUNT_ID_REQUIRED_MESSAGE)
    @PositiveOrZero(message = FIELD_DISCOUNT_ID_CANNOT_BE_NEGATIVE_MESSAGE)
    private Long discountId;
}
