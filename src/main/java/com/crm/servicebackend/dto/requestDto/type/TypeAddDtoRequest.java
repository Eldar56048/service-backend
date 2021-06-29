package com.crm.servicebackend.dto.requestDto.type;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class TypeAddDtoRequest {
    @NotBlank(message = "Поле название опыта обязательно.")
    @Length(message = "Длина название опыта должна быть больше нуля", min = 1)
    private String name;
}
