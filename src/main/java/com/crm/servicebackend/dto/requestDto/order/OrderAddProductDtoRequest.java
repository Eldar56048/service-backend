package com.crm.servicebackend.dto.requestDto.order;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class OrderAddProductDtoRequest {
    @NotNull(message = "Поле id обязательно")
    @Positive(message = "id не может быть негативным числом")
    private Long product_id;
    @Min(value = 1,message = "Значение поле должно быть больше 0")
    @Max(value = 100, message = "Значение поле должно быть меньше 100")
    private int quantity;
}

