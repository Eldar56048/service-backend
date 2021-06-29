package com.crm.servicebackend.controller;

import com.crm.servicebackend.dto.requestDto.user.UserAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.user.UserUpdateDtoRequest;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.service.ExperienceModelService;
import com.crm.servicebackend.service.ServiceCenterService;
import com.crm.servicebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@PreAuthorize("isAuthenticated()&&hasAuthority('ADMIN')")
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
            @RequestParam Long serviceCenterId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String orderBy,
            @RequestParam(defaultValue = "") String title
    ) {
        Map<String, Object> response;
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (title.length()<=0)
            response = service.getAll(serviceCenterId,page-1, size, sortBy, orderBy);
        else
            response = service.getAllAndFilter(serviceCenterId,page-1, size, sortBy, orderBy, title);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @Valid @RequestBody UserAddDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдена", "service-center/not-found");
        if (!experienceModelService.existsByIdAndServiceCenterId(dto.getExperienceModelId(), serviceCenterId))
            throw new ResourceNotFoundException("Опыт с идентификатором № "+dto.getExperienceModelId()+" не найдено", "experience-model/not-found");
        return ResponseEntity.ok(service.add(serviceCenterId, dto, user));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user, @Valid @RequestBody UserUpdateDtoRequest dto, @PathVariable Long userId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if (dto.getId()!=userId)
            throw new DtoException("Два разных id", "user/two-another-id");
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдена", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(dto.getId(), serviceCenterId))
            throw new ResourceNotFoundException("Пользователь с идентификатором № "+dto.getId()+" не найдено", "user/not-found");
        if (!experienceModelService.existsByIdAndServiceCenterId(dto.getExperienceModelId(), serviceCenterId))
            throw new ResourceNotFoundException("Опыт с идентификатором № "+dto.getExperienceModelId()+" не найдено", "experience-model/not-found");
        return ResponseEntity.ok(service.update(userId, serviceCenterId, dto, user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> get(@AuthenticationPrincipal User user, @PathVariable Long userId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдена", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(userId, serviceCenterId))
            throw new ResourceNotFoundException("Пользователь с идентификатором № "+userId+" не найдено", "user/not-found");
        return ResponseEntity.ok(service.get(userId, serviceCenterId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user, @PathVariable Long userId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдена", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(userId, serviceCenterId))
            throw new ResourceNotFoundException("Пользователь с идентификатором № "+userId+" не найдено", "user/not-found");
        service.delete(userId);
        return ResponseEntity.ok("user/deleted");
    }
}
