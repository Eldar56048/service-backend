package com.crm.servicebackend.dto.requestDto.product;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
public class ProductAddDtoRequest {
    @NotBlank(message = "Поле наименование товара обязательно.")
    @Length(message = "Длина наименование товара должна быть больше нуля", min = 1)
    private String name;
    @NotBlank(message = "Полe Описание товара обязательно.")
    @Length(message = "Длина описание товара должна быть больше нуля", min = 1)
    private String description;
    @NotNull(message = "Поле цена обязательно.")
    @PositiveOrZero(message = "Значение поле должно быть положительным")
    private int price;
}
