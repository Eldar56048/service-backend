package com.crm.servicebackend.dto.requestDto.discount;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
public class DiscountAddDtoRequest {
    @NotBlank(message = "Поле название скидки обязательно.")
    @Length(message = "Длина название скидки должна быть больше нуля", min = 1)
    private String discountName;
    @NotNull(message = "Поле процент обязательно.")
    @Min(value = 1,message = "Значение поле должно быть больше 1")
    @Max(value = 100, message = "Значение поле должно быть меньше 100")
    private int percentage;
}
