package com.crm.servicebackend.dto.requestDto.providers;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Data
public class ProviderUpdateDtoRequest {
    @NotNull(message = "Поле id обязательно")
    @PositiveOrZero(message = "id не может быть негативным числом")
    private Long id;
    @NotBlank(message = "Полe Имя обязательно.")
    @Length(message = "Длина Имени должна быть больше нуля", min = 1)
    private String name;
    @NotBlank(message = "Полe Адрес обязательно.")
    @Length(message = "Длина адреса должна быть больше нуля", min = 1)
    private String address;
    @NotBlank(message = "Телефон номер обязательно.")
    @Pattern(regexp = "[8][0-9]{10}", message = "Номер телефон не соответствует формату")
    private String phoneNumber;
}
