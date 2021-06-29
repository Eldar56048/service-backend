package com.crm.servicebackend.dto.requestDto.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
public class ModelUpdateDtoRequest {
    @NotNull(message = "Поле id обязательно")
    @PositiveOrZero(message = "id не может быть негативным числом")
    private Long id;
    @Length(message = "Длина название опыта должна быть больше нуля", min = 1)
    private String name;
}
