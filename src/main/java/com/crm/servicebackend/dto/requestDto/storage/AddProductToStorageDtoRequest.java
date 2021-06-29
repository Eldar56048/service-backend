package com.crm.servicebackend.dto.requestDto.storage;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
public class AddProductToStorageDtoRequest {
    @NotNull(message = "Поле id поставщика обязательно")
    @PositiveOrZero(message = "id не может быть негативным числом")
    private long providerId;
    @NotNull(message = "Поле цена обязательно.")
    @PositiveOrZero(message = "Значение поле должно быть положительным")
    private int price;
    @NotNull(message = "Поле количество обязательно.")
    @Positive(message = "Значение поле должно быть положительным")
    private int quantity;
}
