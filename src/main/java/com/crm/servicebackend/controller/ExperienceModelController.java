package com.crm.servicebackend.controller;

import com.crm.servicebackend.dto.requestDto.experienceModel.ExperienceModelAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.experienceModel.ExperienceModelUpdateDtoRequest;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.service.ExperienceModelService;
import com.crm.servicebackend.service.ServiceCenterService;
import com.crm.servicebackend.utils.facade.ExperienceModelFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.crm.servicebackend.constant.model.experienceModel.ExperienceModelResponseCode.*;
import static com.crm.servicebackend.constant.model.experienceModel.ExperienceModelResponseMessage.EXPERIENCE_MODEL_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.experienceModel.ExperienceModelResponseMessage.EXPERIENCE_MODEL_TWO_ANOTHER_ID_MESSAGE;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseCode.SERVICE_CENTER_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseMessage.SERVICE_CENTER_NOT_FOUND_MESSAGE;

@RestController
@RequestMapping("/api/v1/experience-models")
@PreAuthorize("hasAuthority('ADMIN')")
public class ExperienceModelController {

    private final ExperienceModelService service;
    private final ServiceCenterService serviceCenterService;

    @Autowired
    public ExperienceModelController(ExperienceModelService service, ServiceCenterService serviceCenterService) {
        this.service = service;
        this.serviceCenterService = serviceCenterService;
    }

    @GetMapping
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
    public ResponseEntity<?> getAllForSelect(@AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.getAll(serviceCenterId));
    }

    @PutMapping
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @Valid @RequestBody ExperienceModelAddDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.add(serviceCenterId, dto));
    }

    @GetMapping("/{experienceId}")
    public ResponseEntity<?> get(@AuthenticationPrincipal User user, @PathVariable Long experienceId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(experienceId, serviceCenterId))
            throw new ResourceNotFoundException(EXPERIENCE_MODEL_NOT_FOUND_MESSAGE(experienceId), EXPERIENCE_MODEL_NOT_FOUND_CODE);
        return ResponseEntity.ok(ExperienceModelFacade.modelToDtoResponse(service.get(experienceId, serviceCenterId)));
    }

    @PostMapping("/{experienceId}")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user, @PathVariable Long experienceId, @Valid @RequestBody ExperienceModelUpdateDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if (!dto.getId().equals(experienceId))
            throw new DtoException(EXPERIENCE_MODEL_TWO_ANOTHER_ID_MESSAGE, EXPERIENCE_MODEL_TWO_ANOTHER_ID_CODE);
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(experienceId, serviceCenterId))
            throw new ResourceNotFoundException(EXPERIENCE_MODEL_NOT_FOUND_MESSAGE(experienceId), EXPERIENCE_MODEL_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.update(experienceId, serviceCenterId, dto));
    }

    @DeleteMapping("/{experienceId}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user,@PathVariable Long experienceId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(experienceId, serviceCenterId))
            throw new ResourceNotFoundException(EXPERIENCE_MODEL_NOT_FOUND_MESSAGE(experienceId), EXPERIENCE_MODEL_NOT_FOUND_CODE);
        service.delete(experienceId);
        return ResponseEntity.ok(EXPERIENCE_MODEL_DELETED_CODE);
    }
}
