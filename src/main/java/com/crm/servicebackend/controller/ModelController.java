package com.crm.servicebackend.controller;

import com.crm.servicebackend.dto.requestDto.model.ModelAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.model.ModelUpdateDtoRequest;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.service.ModelService;
import com.crm.servicebackend.service.ServiceCenterService;
import com.crm.servicebackend.utils.facade.ModelFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.crm.servicebackend.constant.model.model.ModelResponseCode.*;
import static com.crm.servicebackend.constant.model.model.ModelResponseMessage.MODEL_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.model.ModelResponseMessage.MODEL_TWO_ANOTHER_ID_MESSAGE;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseCode.SERVICE_CENTER_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseMessage.SERVICE_CENTER_NOT_FOUND_MESSAGE;

@RestController
@RequestMapping("/api/v1/models")
public class ModelController {
    private final ModelService service;
    private final ServiceCenterService serviceCenterService;

    @Autowired
    public ModelController(ModelService service, ServiceCenterService serviceCenterService) {
        this.service = service;
        this.serviceCenterService = serviceCenterService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
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
    @PreAuthorize("hasAuthority('USER')")
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
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @Valid @RequestBody ModelAddDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.add(serviceCenterId, dto));
    }

    @GetMapping("/{modelId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> get(@AuthenticationPrincipal User user, @PathVariable Long modelId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(modelId, serviceCenterId))
            throw new ResourceNotFoundException(MODEL_NOT_FOUND_MESSAGE(modelId), MODEL_NOT_FOUND_CODE);
        return ResponseEntity.ok(ModelFacade.modelToDtoResponse(service.get(modelId, serviceCenterId)));
    }

    @PostMapping("/{modelId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user, @PathVariable Long modelId, @Valid @RequestBody ModelUpdateDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if (dto.getId()!=modelId)
            throw new DtoException(MODEL_TWO_ANOTHER_ID_MESSAGE, MODEL_TWO_ANOTHER_ID_CODE);
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(modelId, serviceCenterId))
            throw new ResourceNotFoundException(MODEL_NOT_FOUND_MESSAGE(modelId), MODEL_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.update(modelId, serviceCenterId, dto));
    }

    @DeleteMapping("/{modelId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user,@PathVariable Long modelId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(modelId, serviceCenterId))
            throw new ResourceNotFoundException(MODEL_NOT_FOUND_MESSAGE(modelId), MODEL_NOT_FOUND_CODE);
        service.delete(modelId);
        return ResponseEntity.ok(MODEL_DELETED_CODE);
    }
}
