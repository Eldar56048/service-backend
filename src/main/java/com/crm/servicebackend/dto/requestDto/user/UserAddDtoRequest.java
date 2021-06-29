package com.crm.servicebackend.dto.requestDto.user;

import com.crm.servicebackend.model.Role;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Set;

@Data
public class UserAddDtoRequest {
    @NotBlank(message = "Полe Имя обязательно.")
    @Length(message = "Длина Имени должна быть больше нуля", min = 1)
    private String name;
    @NotBlank(message = "Поле Фамилия обязательно.")
    @Length(message = "Длина Фамилии должна быть больше нуля", min = 1)
    private String surname;
    @NotBlank(message = "Поле username обязательно.")
    @Length(message = "Длина username должна быть больше нуля", min = 1)
    private String username;
    @NotBlank(message = "Поле password обязательно.")
    @Length(message = "Длина password должна быть больше нуля", min = 1)
    private String password;
    @NotEmpty(message = "Поле роли обязательно")
    private Set<Role> roles;
    @NotBlank(message = "Телефон номер обязательно.")
    @Pattern(regexp = "[8][0-9]{10}", message = "Номер телефон не соответствует формату")
    private String phoneNumber;
    @NotNull(message = "Поле id обязательно")
    @PositiveOrZero(message = "id не может быть негативным числом")
    private Long experienceModelId;
}
