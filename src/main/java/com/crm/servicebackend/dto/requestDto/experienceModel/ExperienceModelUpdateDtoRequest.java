package com.crm.servicebackend.dto.requestDto.experienceModel;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
public class ExperienceModelUpdateDtoRequest {
    @NotNull(message = "Поле id обязательно")
    @Positive(message = "id не может быть негативным числом")
    private Long id;
    @NotBlank(message = "Поле название опыта обязательно.")
    @Length(message = "Длина название опыта должна быть больше нуля", min = 1)
    private String name;
    @Min(value = 0,message = "Значение поле должно быть больше нуля")
    @Max(value = 100, message = "Значение поле должно быть меньше 100")
    private int coefficient;
}
