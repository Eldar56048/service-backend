package com.crm.servicebackend.controller;

import com.crm.servicebackend.dto.requestDto.service.ServiceAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.service.ServiceUpdateDtoRequest;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.service.ServiceCenterService;
import com.crm.servicebackend.service.ServiceModelService;
import com.crm.servicebackend.utils.facade.ServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.crm.servicebackend.constant.response.service.ServiceResponseCode.*;
import static com.crm.servicebackend.constant.response.service.ServiceResponseMessage.SERVICE_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.response.service.ServiceResponseMessage.SERVICE_TWO_ANOTHER_ID_MESSAGE;
import static com.crm.servicebackend.constant.response.serviceCenter.ServiceCenterResponseCode.SERVICE_CENTER_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.response.serviceCenter.ServiceCenterResponseMessage.SERVICE_CENTER_NOT_FOUND_MESSAGE;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {
    private final ServiceModelService service;
    private final ServiceCenterService serviceCenterService;

    @Autowired
    public ServiceController(ServiceModelService service, ServiceCenterService serviceCenterService) {
        this.service = service;
        this.serviceCenterService = serviceCenterService;
    }

    @GetMapping("/select")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getAllForSelect(@AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.getServicesForSelect(serviceCenterId));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> getAll(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String orderBy,
            @RequestParam(defaultValue = "") String title
    ) {
        Map<String, Object> response;
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (title.length()<=0)
            response = service.getAll(serviceCenterId,page-1, size, sortBy, orderBy);
        else
            response = service.getAllAndFilter(serviceCenterId,page-1, size, sortBy, orderBy, title);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @Valid @RequestBody ServiceAddDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.add(serviceCenterId, dto, user));
    }

    @GetMapping("/{serviceId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> get(@AuthenticationPrincipal User user, @PathVariable Long serviceId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(serviceId, serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_NOT_FOUND_MESSAGE(serviceId), SERVICE_NOT_FOUND_CODE);
        return ResponseEntity.ok(ServiceFacade.modelToDtoResponse(service.get(serviceId, serviceCenterId)));
    }

    @GetMapping("/{serviceId}/sold")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> getServiceSoldCount(@AuthenticationPrincipal User user,@PathVariable long serviceId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(serviceId, serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_NOT_FOUND_MESSAGE(serviceId), SERVICE_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.getServiceSoldCount(serviceCenterId, serviceId).getSoldCount());
    }

    @GetMapping("/{serviceId}/monthly/sold")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getMonthlySold(@PathVariable long serviceId, @AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(serviceId, serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_NOT_FOUND_MESSAGE(serviceId), SERVICE_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.getMonthlySold(serviceCenterId, serviceId));
    }

    @GetMapping("/top-profit-services")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getTopProfitServices(@AuthenticationPrincipal User user, @RequestParam(defaultValue = "5") int limit) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.getTopProfitServices(serviceCenterId, limit));
    }

    @GetMapping("/top-sale")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getTopSale(@AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.getTopSaleService(serviceCenterId));
    }

    @GetMapping("/sold-count")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getSoldServiceCount(@AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.getSoldServiceCount(serviceCenterId));
    }

    @PostMapping("/{serviceId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user, @PathVariable Long serviceId, @Valid @RequestBody ServiceUpdateDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if (dto.getId()!=serviceId)
            throw new DtoException(SERVICE_TWO_ANOTHER_ID_MESSAGE, SERVICE_TWO_ANOTHER_ID_CODE);
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(serviceId, serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_NOT_FOUND_MESSAGE(serviceId), SERVICE_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.update(serviceId, serviceCenterId, dto, user));
    }

    @DeleteMapping("/{serviceId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user,@PathVariable Long serviceId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(serviceId, serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_NOT_FOUND_MESSAGE(serviceId), SERVICE_NOT_FOUND_CODE);
        service.delete(serviceId, serviceCenterId);
        return ResponseEntity.ok(SERVICE_DELETED_CODE);
    }

}
