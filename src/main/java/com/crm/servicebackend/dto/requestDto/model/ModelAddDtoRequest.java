package com.crm.servicebackend.dto.requestDto.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class ModelAddDtoRequest {
    @NotBlank(message = "Поле название опыта обязательно.")
    @Length(message = "Длина название опыта должна быть больше нуля", min = 1)
    private String name;
}
