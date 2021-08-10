package com.crm.servicebackend.utils.facade;

import com.crm.servicebackend.dto.responseDto.document.DocumentDtoResponse;
import com.crm.servicebackend.model.Document;

import java.util.ArrayList;
import java.util.List;

public class DocumentFacade {
    public static DocumentDtoResponse modelToDtoResponse(Document model) {
        DocumentDtoResponse dto = new DocumentDtoResponse();
        if(model.getId() != null)
            dto.setId(model.getId());
        if(model.getCreated() != null)
            dto.setCreated(model.getCreated());
        if(model.getPrinted() != null)
            dto.setCreated(model.getCreated());
        if(model.getSigned() != null)
            dto.setSigned(model.getSigned());
        if(model.getOrder() != null)
            dto.setOrder(OrderFacade.modelToOrderDtoResponse(model.getOrder()));
        if(model.getDocumentType() != null)
            dto.setDocumentType(model.getDocumentType());
        if(model.getDocumentStatus() != null)
            dto.setDocumentStatus(model.getDocumentStatus());
        return dto;
    }

    public static List<DocumentDtoResponse> modelListToDtoResponseList(List<Document> modelList) {
        List<DocumentDtoResponse> dtoList = new ArrayList<>();
        for(Document model : modelList) {
            dtoList.add(modelToDtoResponse(model));
        }
        return dtoList;
    }
}
