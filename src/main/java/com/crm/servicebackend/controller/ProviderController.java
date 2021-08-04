package com.crm.servicebackend.controller;

import com.crm.servicebackend.dto.requestDto.providers.ProviderAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.providers.ProviderUpdateDtoRequest;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.service.ProviderService;
import com.crm.servicebackend.service.ServiceCenterService;
import com.crm.servicebackend.utils.facade.ProviderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.crm.servicebackend.constant.model.provider.ProviderResponseCode.*;
import static com.crm.servicebackend.constant.model.provider.ProviderResponseMessage.PROVIDER_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.provider.ProviderResponseMessage.PROVIDER_TWO_ANOTHER_ID_MESSAGE;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseCode.SERVICE_CENTER_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseMessage.SERVICE_CENTER_NOT_FOUND_MESSAGE;

@RestController
@RequestMapping("/api/v1/providers")
public class ProviderController {
    private final ProviderService service;
    private final ServiceCenterService serviceCenterService;

    @Autowired
    public ProviderController(ProviderService service, ServiceCenterService serviceCenterService) {
        this.service = service;
        this.serviceCenterService = serviceCenterService;
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

    @GetMapping("/select")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> getAllForSelect(
            @AuthenticationPrincipal User user
    ) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.getAllForSelect(serviceCenterId));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @Valid @RequestBody ProviderAddDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.add(serviceCenterId, dto));
    }

    @GetMapping("/{providerId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> get(@AuthenticationPrincipal User user, @PathVariable Long providerId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(providerId, serviceCenterId))
            throw new ResourceNotFoundException(PROVIDER_NOT_FOUND_MESSAGE(providerId), PROVIDER_NOT_FOUND_CODE);
        return ResponseEntity.ok(ProviderFacade.modelToDtoResponse(service.get(providerId, serviceCenterId)));
    }

    @PostMapping("/{providerId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user, @PathVariable Long providerId, @Valid @RequestBody ProviderUpdateDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if (!dto.getId().equals(providerId))
            throw new DtoException(PROVIDER_TWO_ANOTHER_ID_MESSAGE, PROVIDER_TWO_ANOTHER_ID_CODE);
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(providerId, serviceCenterId))
            throw new ResourceNotFoundException(PROVIDER_NOT_FOUND_MESSAGE(providerId), PROVIDER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.update(providerId, serviceCenterId, dto));
    }

    @DeleteMapping("/{providerId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user,@PathVariable Long providerId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(providerId, serviceCenterId))
            throw new ResourceNotFoundException(PROVIDER_NOT_FOUND_MESSAGE(providerId), PROVIDER_NOT_FOUND_CODE);
        service.delete(providerId);
        return ResponseEntity.ok(PROVIDER_DELETED_CODE);
    }
}
