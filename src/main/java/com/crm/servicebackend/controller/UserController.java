package com.crm.servicebackend.controller;

import com.crm.servicebackend.dto.requestDto.user.UserAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.user.UserUpdateDtoRequest;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.model.enums.Role;
import com.crm.servicebackend.service.ExperienceModelService;
import com.crm.servicebackend.service.ServiceCenterService;
import com.crm.servicebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.crm.servicebackend.constant.model.experienceModel.ExperienceModelResponseCode.EXPERIENCE_MODEL_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.experienceModel.ExperienceModelResponseMessage.EXPERIENCE_MODEL_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseCode.SERVICE_CENTER_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseMessage.SERVICE_CENTER_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.user.UserResponseCode.*;
import static com.crm.servicebackend.constant.model.user.UserResponseMessage.USER_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.user.UserResponseMessage.USER_TWO_ANOTHER_ID_MESSAGE;

@RestController
@RequestMapping("/api/v1/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    private final UserService service;
    private final ExperienceModelService experienceModelService;
    private final ServiceCenterService serviceCenterService;

    @Autowired
    public UserController(UserService service, ExperienceModelService experienceModelService, ServiceCenterService serviceCenterService) {
        this.service = service;
        this.experienceModelService = experienceModelService;
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

    @GetMapping("/roles/all")
    public ResponseEntity<?> getAllRoles(){
        Role[] roles = (Role.values());
        List<Role> roleList = new ArrayList<>();
        for(Role role: roles){
            if (!role.equals(Role.OWNER))
                roleList.add(role);
        }
        return ResponseEntity.ok(roleList);
    }

    @PutMapping
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @Valid @RequestBody UserAddDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!experienceModelService.existsByIdAndServiceCenterId(dto.getExperienceModelId(), serviceCenterId))
            throw new ResourceNotFoundException(EXPERIENCE_MODEL_NOT_FOUND_MESSAGE(dto.getExperienceModelId()), EXPERIENCE_MODEL_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.add(serviceCenterId, dto, user));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user, @Valid @RequestBody UserUpdateDtoRequest dto, @PathVariable Long userId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if (dto.getId()!=userId)
            throw new DtoException(USER_TWO_ANOTHER_ID_MESSAGE, USER_TWO_ANOTHER_ID_CODE);
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(dto.getId(), serviceCenterId))
            throw new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE(dto.getId()), USER_NOT_FOUND_CODE);
        if (!experienceModelService.existsByIdAndServiceCenterId(dto.getExperienceModelId(), serviceCenterId))
            throw new ResourceNotFoundException(EXPERIENCE_MODEL_NOT_FOUND_MESSAGE(dto.getExperienceModelId()), EXPERIENCE_MODEL_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.update(userId, serviceCenterId, dto, user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> get(@AuthenticationPrincipal User user, @PathVariable Long userId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(userId, serviceCenterId))
            throw new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE(userId), USER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.get(userId, serviceCenterId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user, @PathVariable Long userId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(userId, serviceCenterId))
            throw new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE(userId), USER_NOT_FOUND_CODE);
        service.delete(userId);
        return ResponseEntity.ok(USER_DELETED_CODE);
    }
}
