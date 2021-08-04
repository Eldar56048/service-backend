package com.crm.servicebackend.dto.requestDto.discount;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

import static com.crm.servicebackend.constant.model.discount.DiscountValidationConstants.*;

@Data
public class DiscountUpdateDtoRequest {

    @NotNull(message = FIELD_ID_REQUIRED_MESSAGE)
    @PositiveOrZero(message = FIELD_ID_CANNOT_BE_NEGATIVE_MESSAGE)
    private Long id;

    @NotBlank(message = FIELD_DISCOUNT_NAME_REQUIRED_MESSAGE)
    @Length(message = FIELD_DISCOUNT_NAME_LENGTH_MESSAGE, min = 1)
    private String discountName;

    @NotNull(message = FIELD_PERCENTAGE_REQUIRED_MESSAGE)
    @Min(value = 1,message = FIELD_PERCENTAGE_MIN_VALUE_MESSAGE)
    @Max(value = 100, message = FIELD_PERCENTAGE_MAX_VALUE_MESSAGE)
    private int percentage;
}
