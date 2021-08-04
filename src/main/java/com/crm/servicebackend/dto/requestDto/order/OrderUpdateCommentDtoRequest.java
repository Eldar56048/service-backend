package com.crm.servicebackend.dto.requestDto.order;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

import static com.crm.servicebackend.constant.model.order.OrderValidationConstants.FIELD_COMMENT_LENGTH_MESSAGE;
import static com.crm.servicebackend.constant.model.order.OrderValidationConstants.FIELD_COMMENT_REQUIRED_MESSAGE;

@Data
public class OrderUpdateCommentDtoRequest {

    @NotBlank(message = FIELD_COMMENT_REQUIRED_MESSAGE)
    @Length(message = FIELD_COMMENT_LENGTH_MESSAGE, min = 1)
    private String comment;

}
