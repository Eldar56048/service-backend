package com.crm.servicebackend.dto.requestDto.serviceCenter;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
public class ServiceCenterUpdateDtoRequest {
    @NotNull(message = "Поле Id обязательно.")
    @PositiveOrZero(message = "id не может быть негативным числом")
    private Long id;

    @NotBlank(message = "Имя обязательно.")
    @Length(message = "Длина имени должна быть больше нуля", min = 1)
    private String name;

    @NotBlank(message = "Адрес обязательно.")
    private String address;

    @NotBlank(message = "Телефон номер обязательно.")
    @Pattern(regexp = "[8][0-9]{10}", message = "Номер телефон не соответствует формату")
    private String phoneNumber;

    @NotBlank(message = "Email обязательно.")
    @Email(message = "Не соответсвует формату email.")
    private String email;

    @NotNull(message = "Поле Активен обязательно.")
    private Boolean isEnabled;

    @NotBlank(message = "Комментарии обязательно.")
    private String comment;
}
