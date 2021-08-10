package com.crm.servicebackend.dto.requestDto.document;

import com.crm.servicebackend.model.enums.DocumentType;
import lombok.Data;

import javax.validation.constraints.NotNull;

import static com.crm.servicebackend.constant.model.document.DocumentValidationConstants.FIELD_DOCUMENT_TYPE_REQUIRED_MESSAGE;
import static com.crm.servicebackend.constant.model.document.DocumentValidationConstants.FIELD_ORDER_ID_REQUIRED_MESSAGE;

@Data
public class DocumentAddDtoRequest {
    @NotNull(message = FIELD_ORDER_ID_REQUIRED_MESSAGE)
    private Long orderId;
    @NotNull(message = FIELD_DOCUMENT_TYPE_REQUIRED_MESSAGE)
    private DocumentType documentType;
}
