package com.crm.servicebackend.dto.responseDto.document;

import com.crm.servicebackend.dto.responseDto.order.OrderDtoResponse;
import com.crm.servicebackend.model.enums.DocumentStatus;
import com.crm.servicebackend.model.enums.DocumentType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class DocumentDtoResponse {
    private Long id;
    private Date created;
    private Date printed;
    private Date signed;
    private OrderDtoResponse order;
    private DocumentType documentType;
    private DocumentStatus documentStatus;
}
