package com.crm.servicebackend.service;

import com.crm.servicebackend.dto.requestDto.document.DocumentChangeStatusDtoRequest;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.model.Document;
import com.crm.servicebackend.model.enums.DocumentStatus;
import com.crm.servicebackend.model.enums.DocumentType;
import com.crm.servicebackend.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.crm.servicebackend.constant.model.document.DocumentResponseCode.DOCUMENT_INVALID_DOCUMENT_STATUS_CODE;
import static com.crm.servicebackend.constant.model.document.DocumentResponseCode.DOCUMENT_STATUS_NOT_CHANGED_CODE;
import static com.crm.servicebackend.constant.model.document.DocumentResponseMessage.DOCUMENT_INVALID_DOCUMENT_STATUS_MESSAGE;
import static com.crm.servicebackend.constant.model.document.DocumentResponseMessage.DOCUMENT_STATUS_NOT_CHANGED_MESSAGE;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ServiceCenterService serviceCenterService;

    public Document createDocument(Long orderId, DocumentType documentType, Long serviceCenterId) {
        Document document = new Document(new Date(), orderService.get(orderId, serviceCenterId), documentType, DocumentStatus.CREATED, serviceCenterService.get(serviceCenterId) );
        return save(document);
    }

    public List<Document> getAllByOrder(Long orderId, Long serviceCenterId) {
        return documentRepository.getAllByOrderIdAndServiceCenterId(orderId, serviceCenterId);
    }

    public Document documentStatusPrinted(Long documentId, Long serviceCenterId) {
        Document document = get(documentId, serviceCenterId);
        if (document.getDocumentStatus() != DocumentStatus.CREATED) {
            throw new DtoException(DOCUMENT_STATUS_NOT_CHANGED_MESSAGE, DOCUMENT_STATUS_NOT_CHANGED_CODE);
        }
        document.setDocumentStatus(DocumentStatus.PRINTED);
        document.setPrinted(new Date());
        return save(document);
    }

    public Document documentStatusSigned(Long documentId, Long serviceCenterId) {
        Document document = get(documentId, serviceCenterId);
        if (document.getDocumentStatus() != DocumentStatus.PRINTED) {
            throw new DtoException(DOCUMENT_STATUS_NOT_CHANGED_MESSAGE, DOCUMENT_STATUS_NOT_CHANGED_CODE);
        }
        document.setDocumentStatus(DocumentStatus.SIGNED);
        document.setSigned(new Date());
        return save(document);
    }

    public Document changeDocumentStatus(DocumentChangeStatusDtoRequest dto, Long serviceCenterId) {
        switch (dto.getDocumentStatus()) {
            case PRINTED: {
                return documentStatusPrinted(dto.getDocumentId(), serviceCenterId);
            }
            case SIGNED: {
                return documentStatusSigned(dto.getDocumentId(), serviceCenterId);
            }
            default: {
                throw new DtoException(DOCUMENT_INVALID_DOCUMENT_STATUS_MESSAGE, DOCUMENT_INVALID_DOCUMENT_STATUS_CODE);
            }
        }
    }

    public void deleteDocument(Long documentId) {
        documentRepository.deleteById(documentId);
    }

    public Document get(Long documentId, Long serviceCenterId) {
        return documentRepository.findByIdAndServiceCenterId(documentId, serviceCenterId);
    }

    public boolean existsByOrderIdAndServiceCenterId(Long orderId, Long serviceCenterId) {
        return documentRepository.existsByOrderIdAndServiceCenterId(orderId, serviceCenterId);
    }

    public boolean existsByOrderIdAndDocumentType(Long orderId, DocumentType documentType) {
        return documentRepository.existsByOrderIdAndAndDocumentType(orderId, documentType);
    }

    public boolean existsByIdAndServiceCenterId(Long documentId, Long serviceCenterId) {
        return documentRepository.existsByIdAndServiceCenterId(documentId, serviceCenterId);
    }

    public boolean existsByIdAndOrderIdAndServiceCenterId(Long documentId, Long orderId, Long serviceCenterId) {
        return documentRepository.existsByIdAndOrderIdAndServiceCenterId(documentId, orderId, serviceCenterId);
    }

    public Document save(Document document) {
        return documentRepository.save(document);
    }
}
