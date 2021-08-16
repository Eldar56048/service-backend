package com.crm.servicebackend.controller;

import com.crm.servicebackend.dto.requestDto.document.DocumentAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.document.DocumentChangeStatusDtoRequest;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.service.DocumentService;
import com.crm.servicebackend.service.OrderService;
import com.crm.servicebackend.service.ServiceCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.crm.servicebackend.constant.model.document.DocumentResponseCode.DOCUMENT_FOR_ORDER_ALREADY_CREATED_CODE;
import static com.crm.servicebackend.constant.model.document.DocumentResponseCode.DOCUMENT_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.document.DocumentResponseMessage.DOCUMENT_FOR_ORDER_ALREADY_CREATED_MESSAGE;
import static com.crm.servicebackend.constant.model.document.DocumentResponseMessage.DOCUMENT_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.order.OrderResponseCode.ORDER_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.order.OrderResponseMessage.ORDER_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseCode.SERVICE_CENTER_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseMessage.SERVICE_CENTER_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.utils.facade.DocumentFacade.*;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ServiceCenterService serviceCenterService;

    @PutMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> create(@Valid @RequestBody DocumentAddDtoRequest dto, @AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if(!orderService.existsByIdAndServiceCenterId(dto.getOrderId(), serviceCenterId))
            throw new ResourceNotFoundException(ORDER_NOT_FOUND_MESSAGE(dto.getOrderId()), ORDER_NOT_FOUND_CODE);
        if(documentService.existsByOrderIdAndDocumentType(dto.getOrderId(), dto.getDocumentType()))
            throw new DtoException(DOCUMENT_FOR_ORDER_ALREADY_CREATED_MESSAGE, DOCUMENT_FOR_ORDER_ALREADY_CREATED_CODE);
        return ResponseEntity.ok(modelToDocumentOrderDtoResponse(documentService.createDocument(dto.getOrderId(), dto.getDocumentType(), serviceCenterId)));
    }

    @GetMapping("/{documentId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> get(@PathVariable Long documentId, @AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if(!documentService.existsByIdAndServiceCenterId(documentId, serviceCenterId))
            throw new DtoException(DOCUMENT_NOT_FOUND_MESSAGE, DOCUMENT_NOT_FOUND_CODE);
        return ResponseEntity.ok(modelToDtoResponse(documentService.get(documentId, serviceCenterId)));
    }

    @PostMapping("/status")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> changeStatus(@Valid @RequestBody DocumentChangeStatusDtoRequest dto, @AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if(!orderService.existsByIdAndServiceCenterId(dto.getOrderId(), serviceCenterId))
            throw new ResourceNotFoundException(ORDER_NOT_FOUND_MESSAGE(dto.getOrderId()), ORDER_NOT_FOUND_CODE);
        if(!documentService.existsByIdAndOrderIdAndServiceCenterId(dto.getDocumentId(), dto.getOrderId(), serviceCenterId))
            throw new DtoException(DOCUMENT_NOT_FOUND_MESSAGE, DOCUMENT_NOT_FOUND_CODE);
        return ResponseEntity.ok(modelToDtoResponse(documentService.changeDocumentStatus(dto, serviceCenterId)));
    }

    @GetMapping("/orders/{orderId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getDocumentsByOrder(@PathVariable Long orderId, @AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if(!orderService.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException(ORDER_NOT_FOUND_MESSAGE(orderId), ORDER_NOT_FOUND_CODE);
        return ResponseEntity.ok(modelListToDocumentOrderDtoList(documentService.getAllByOrder(orderId, serviceCenterId)));
    }
}
