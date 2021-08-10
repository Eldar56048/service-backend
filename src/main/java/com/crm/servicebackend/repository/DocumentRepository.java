package com.crm.servicebackend.repository;

import com.crm.servicebackend.model.Document;
import com.crm.servicebackend.model.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Boolean existsByIdAndServiceCenterId(Long documentId, Long serviceCenterId);
    Boolean existsByOrderIdAndServiceCenterId(Long orderId, Long serviceCenterId);
    Boolean existsByOrderIdAndAndDocumentType(Long orderId, DocumentType documentType);
    Boolean existsByIdAndOrderIdAndServiceCenterId(Long documentId, Long orderId, Long serviceCenterId);
    Document findByIdAndServiceCenterId(Long documentId, Long serviceCenterId);
    List<Document> getAllByOrderIdAndServiceCenterId(Long orderId, Long serviceCenterId);
}
