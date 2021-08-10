package com.crm.servicebackend.dto.requestDto.document;

import com.crm.servicebackend.model.enums.DocumentStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

import static com.crm.servicebackend.constant.model.document.DocumentValidationConstants.*;

@Data
public class DocumentChangeStatusDtoRequest {
    @NotNull(message = FIELD_ORDER_ID_REQUIRED_MESSAGE)
    private Long orderId;
    @NotNull(message = FIELD_DOCUMENT_ID_REQUIRED_MESSAGE)
    private Long documentId;
    @NotNull(message = FIELD_DOCUMENT_STATUS_REQUIRED_MESSAGE)
    private DocumentStatus documentStatus;
}
