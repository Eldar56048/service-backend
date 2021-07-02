package com.crm.servicebackend.controller;

import com.crm.servicebackend.dto.requestDto.type.TypeAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.type.TypeUpdateDtoRequest;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.service.ServiceCenterService;
import com.crm.servicebackend.service.TypeService;
import com.crm.servicebackend.utils.facade.TypeFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/types")
public class TypeController {
    private final TypeService service;
    private final ServiceCenterService serviceCenterService;

    @Autowired
    public TypeController(TypeService service, ServiceCenterService serviceCenterService) {
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
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
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
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        return ResponseEntity.ok(service.getAllForSelect(serviceCenterId));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @Valid @RequestBody TypeAddDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        return ResponseEntity.ok(service.add(serviceCenterId, dto));
    }

    @GetMapping("/{typeId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> get(@AuthenticationPrincipal User user, @PathVariable Long typeId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(typeId, serviceCenterId))
            throw new ResourceNotFoundException("Тип устройства с идентификатором № "+typeId+" не найдено", "type/not-found");
        return ResponseEntity.ok(TypeFacade.modelToDtoResponse(service.get(typeId, serviceCenterId)));
    }

    @PostMapping("/{typeId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user, @PathVariable Long typeId, @Valid @RequestBody TypeUpdateDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if (dto.getId()!=typeId)
            throw new DtoException("Два разных id", "type/two-another-id");
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(typeId, serviceCenterId))
            throw new ResourceNotFoundException("Тип устройства с идентификатором № "+typeId+" не найдено", "type/not-found");
        return ResponseEntity.ok(service.update(typeId, serviceCenterId, dto));
    }

    @DeleteMapping("/{typeId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user,@PathVariable Long typeId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(typeId, serviceCenterId))
            throw new ResourceNotFoundException("Тип устройства с идентификатором № "+typeId+" не найдено", "type/not-found");
        service.delete(typeId);
        return ResponseEntity.ok("type/deleted");
    }

}
