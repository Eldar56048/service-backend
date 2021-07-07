package com.crm.servicebackend.dto.requestDto.order;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class OrderUpdateCommentDtoRequest {
    @NotBlank(message = "Полe обязательно")
    @Length(message = "Поле не должно быть пустым", min = 1)
    private String comment;
}
