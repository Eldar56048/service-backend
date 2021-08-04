package com.crm.servicebackend.controller;

import com.crm.servicebackend.dto.requestDto.experienceModel.ExperienceModelAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.serviceCenter.ServiceCenterAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.serviceCenter.ServiceCenterUpdateDtoRequest;
import com.crm.servicebackend.dto.requestDto.user.UserAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.user.UserUpdateDtoRequest;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.model.Role;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.service.ExperienceModelService;
import com.crm.servicebackend.service.ServiceCenterService;
import com.crm.servicebackend.service.UserService;
import com.crm.servicebackend.utils.facade.ServiceCenterFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseCode.*;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseMessage.SERVICE_CENTER_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseMessage.SERVICE_CENTER_TWO_ANOTHER_ID_MESSAGE;

@RestController
@RequestMapping("/api/v1/service-centers")
@PreAuthorize("isAuthenticated() && hasAuthority('OWNER')")

public class ServiceCenterController {

    private final ServiceCenterService service;
    private final ExperienceModelService experienceModelService;
    private final UserService userService;

    @Autowired
    public ServiceCenterController(ServiceCenterService service, ExperienceModelService experienceModelService, UserService userService) {
        this.service = service;
        this.experienceModelService = experienceModelService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String orderBy,
            @RequestParam(defaultValue = "") String title
    ) {
        Map<String, Object> response;
        if (title.length()<=0)
            response = service.getAll(page-1, size, sortBy, orderBy);
        else
            response = service.getAllAndFilter(page-1, size, sortBy, orderBy, title);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> add(@Valid @RequestBody ServiceCenterAddDtoRequest dto) {
        return ResponseEntity.ok(service.add(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        if (!service.existsById(id))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(id), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(ServiceCenterFacade.modelToDtoResponse(service.get(id)));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ServiceCenterUpdateDtoRequest dto) {
        if (dto.getId()!=id)
            throw new DtoException(SERVICE_CENTER_TWO_ANOTHER_ID_MESSAGE, SERVICE_CENTER_TWO_ANOTHER_ID_CODE);
        if (!service.existsById(id))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(id), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!service.existsById(id))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(id), SERVICE_CENTER_NOT_FOUND_CODE);
        service.delete(id);
        return ResponseEntity.ok(SERVICE_CENTER_DELETED_CODE);
    }

    @PutMapping("/{serviceCenterId}/users")
    public ResponseEntity<?> addUser(@AuthenticationPrincipal User user, @Valid @RequestBody UserAddDtoRequest dto, @PathVariable Long serviceCenterId) {
        if(!service.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!experienceModelService.existsByIdAndServiceCenterId(dto.getExperienceModelId(), serviceCenterId))
            throw new ResourceNotFoundException("Опыт с идентификатором № "+dto.getExperienceModelId()+" не найдено", "experience-model/not-found");
        return ResponseEntity.ok(userService.add(serviceCenterId, dto, user));
    }

    @GetMapping("/{serviceCenterId}/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId, @PathVariable Long serviceCenterId) {
        if(!service.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!userService.existsByIdAndServiceCenterId(userId, serviceCenterId))
            throw new ResourceNotFoundException("Пользователь с идентификатором № "+userId+" не найдено", "user/not-found");
        return ResponseEntity.ok(userService.get(userId, serviceCenterId));
    }

    @PostMapping("/{serviceCenterId}/users/{userId}")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal User user, @Valid @RequestBody UserUpdateDtoRequest dto, @PathVariable Long serviceCenterId, @PathVariable Long userId) {
        if (dto.getId()!=userId)
            throw new DtoException("Два разных id", "user/two-another-id");
        if(!service.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!userService.existsByIdAndServiceCenterId(dto.getId(), serviceCenterId))
            throw new ResourceNotFoundException("Пользователь с идентификатором № "+dto.getId()+" не найдено", "user/not-found");
        if (!experienceModelService.existsByIdAndServiceCenterId(dto.getExperienceModelId(), serviceCenterId))
            throw new ResourceNotFoundException("Опыт с идентификатором № "+dto.getExperienceModelId()+" не найдено", "experience-model/not-found");
        return ResponseEntity.ok(userService.update(userId, serviceCenterId, dto, user));
    }

    @GetMapping("/{serviceCenterId}/users")
    public ResponseEntity<?> getUsers(
            @PathVariable Long serviceCenterId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String orderBy,
            @RequestParam(defaultValue = "") String title
    ) {
        Map<String, Object> response;
        if(!service.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (title.length()<=0)
            response = userService.getAll(serviceCenterId,page-1, size, sortBy, orderBy);
        else
            response = userService.getAllAndFilter(serviceCenterId,page-1, size, sortBy, orderBy, title);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{serviceCenterId}/experience-models")
    public ResponseEntity<?> getExperiences(
            @PathVariable Long serviceCenterId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String orderBy,
            @RequestParam(defaultValue = "") String title
    ) {
        Map<String, Object> response;
        if(!service.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (title.length()<=0)
            response = experienceModelService.getAll(serviceCenterId,page-1, size, sortBy, orderBy);
        else
            response = experienceModelService.getAllAndFilter(serviceCenterId,page-1, size, sortBy, orderBy, title);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{serviceCenterId}/experience-models/list")
    public ResponseEntity<?> getExperienceModels(
            @PathVariable Long serviceCenterId
    ) {
        if(!service.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(experienceModelService.getAll(serviceCenterId));
    }

    @PutMapping("/{serviceCenterId}/experience-models")
    public ResponseEntity<?> addExperienceModel(@Valid @RequestBody ExperienceModelAddDtoRequest dto, @PathVariable Long serviceCenterId) {
        if(!service.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(experienceModelService.add(serviceCenterId, dto));
    }

    @GetMapping("/roles/all")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<?> getAllRoles(){
        return ResponseEntity.ok(Role.values());
    }
}
