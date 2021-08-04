package com.crm.servicebackend.dto.requestDto.providers;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.crm.servicebackend.constant.model.provider.ProviderValidationConstants.*;

@Data
public class ProviderAddDtoRequest {

    @NotBlank(message = FIELD_NAME_REQUIRED_MESSAGE)
    @Length(message = FIELD_NAME_LENGTH_MESSAGE, min = 1)
    private String name;

    @NotBlank(message = FIELD_ADDRESS_REQUIRED_MESSAGE)
    @Length(message = FIELD_ADDRESS_LENGTH_MESSAGE, min = 1)
    private String address;

    @NotBlank(message = FIELD_PHONE_NUMBER_REQUIRED_MESSAGE)
    @Pattern(regexp = "[8][0-9]{10}", message = FIELD_PHONE_NUMBER_FORMAT_MESSAGE)
    private String phoneNumber;

}
