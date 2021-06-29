package com.crm.servicebackend.dto.requestDto.experienceModel;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ExperienceModelAddDtoRequest {
    @NotBlank(message = "Поле название опыта обязательно.")
    @Length(message = "Длина название опыта должна быть больше нуля", min = 1)
    private String name;
    @NotNull(message = "Поле коэффициент обязательно.")
    @Min(value = 1,message = "Значение поле должно быть больше 1")
    @Max(value = 100, message = "Значение поле должно быть меньше 100")
    private int coefficient;
}
