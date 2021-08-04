package com.crm.servicebackend.dto.requestDto.order;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static com.crm.servicebackend.constant.model.order.OrderValidationConstants.*;

@Data
public class OrderAddServiceDtoRequest {

    @NotNull(message = FIELD_SERVICE_ID_REQUIRED_MESSAGE)
    @Positive(message = FIELD_SERVICE_ID_CANNOT_BE_NEGATIVE_MESSAGE)
    private Long service_id;

    @NotNull(message = FIELD_QUANTITY_REQUIRED_MESSAGE)
    @Min(value = 1,message = FIELD_QUANTITY_MIN_VALUE_MESSAGE)
    @Max(value = 100, message = FIELD_QUANTITY_MAX_VALUE_MESSAGE)
    private int quantity;

}
