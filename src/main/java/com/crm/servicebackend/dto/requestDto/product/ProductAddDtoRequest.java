package com.crm.servicebackend.dto.requestDto.product;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static com.crm.servicebackend.constant.model.product.ProductValidationConstants.*;

@Data
public class ProductAddDtoRequest {

    @NotBlank(message = FIELD_NAME_REQUIRED_MESSAGE)
    @Length(message = FIELD_NAME_LENGTH_MESSAGE, min = 1)
    private String name;

    @NotBlank(message = FIELD_DESCRIPTION_REQUIRED_MESSAGE)
    @Length(message = FIELD_DESCRIPTION_LENGTH_MESSAGE, min = 1)
    private String description;

    @NotNull(message = FIELD_PRICE_REQUIRED_MESSAGE)
    @Positive(message = FIELD_PRICE_SHOULD_BE_POSITIVE_MESSAGE)
    private int price;

}
