package com.crm.servicebackend.dto.requestDto.discount;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.crm.servicebackend.constant.model.discount.DiscountValidationConstants.*;

@Data
public class DiscountAddDtoRequest {

    @NotBlank(message = FIELD_DISCOUNT_NAME_REQUIRED_MESSAGE)
    @Length(message = FIELD_DISCOUNT_NAME_LENGTH_MESSAGE, min = 1)
    private String discountName;

    @NotNull(message = FIELD_PERCENTAGE_REQUIRED_MESSAGE)
    @Min(value = 0,message = FIELD_PERCENTAGE_MIN_VALUE_MESSAGE)
    @Max(value = 100, message = FIELD_PERCENTAGE_MAX_VALUE_MESSAGE)
    private int percentage;
}
