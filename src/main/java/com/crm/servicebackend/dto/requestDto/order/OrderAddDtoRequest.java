package com.crm.servicebackend.dto.requestDto.order;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
public class OrderAddDtoRequest {
    @NotBlank(message = "Телефон номер обязательно.")
    @Pattern(regexp = "[8][0-9]{10}", message = "Номер телефон не соответствует формату")
    private String client_number;
    @NotBlank(message = "Полe Имя обязательно.")
    @Length(message = "Длина Имени должна быть больше нуля", min = 1)
    private String clientId;
    @NotBlank(message = "Полe обязательно.")
    @Length(message = "Поле не должно быть пустым", min = 1)
    private String problem;
    @NotBlank(message = "Полe обязательно.")
    @Length(message = "Поле не должно быть пустым", min = 1)
    private String modelType;
    @NotNull(message = "Поле id обязательно")
    @Positive(message = "id не может быть негативным числом")
    private Long type_id;
    @NotNull(message = "Поле id обязательно")
    @Positive(message = "id не может быть негативным числом")
    private Long model_id;
    @NotNull(message = "Поле id обязательно")
    @Positive(message = "id не может быть негативным числом")
    private Long discount_id;
}
