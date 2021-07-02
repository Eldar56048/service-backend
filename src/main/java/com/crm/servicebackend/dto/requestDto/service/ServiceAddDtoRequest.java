package com.crm.servicebackend.dto.requestDto.service;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
public class ServiceAddDtoRequest {
    @NotBlank(message = "Поле наименование услуги обязательно.")
    @Length(message = "Длина наименование услуги должна быть больше нуля", min = 1)
    private String name;
    @NotBlank(message = "Полe Описание услуги обязательно.")
    @Length(message = "Длина описание услуги должна быть больше нуля", min = 1)
    private String description;
    @NotNull(message = "Поле процент обязательно.")
    @Min(value = 1,message = "Значение поле должно быть больше 1")
    @Max(value = 100, message = "Значение поле должно быть меньше 100")
    private int percentage;
    @NotNull(message = "Поле цена обязательно.")
    @Positive(message = "Значение поле должно быть положительным")
    private int price;
}
