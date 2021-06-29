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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

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

    @PutMapping
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @Valid @RequestBody ModelAddDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        return ResponseEntity.ok(service.add(serviceCenterId, dto));
    }

    @GetMapping("/{modelId}")
    public ResponseEntity<?> get(@AuthenticationPrincipal User user, @PathVariable Long modelId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(modelId, serviceCenterId))
            throw new ResourceNotFoundException("Модель с идентификатором № "+modelId+" не найдено", "model/not-found");
        return ResponseEntity.ok(ModelFacade.modelToDtoResponse(service.get(modelId, serviceCenterId)));
    }

    @PostMapping("/{modelId}")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user, @PathVariable Long modelId, @Valid @RequestBody ModelUpdateDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if (dto.getId()!=modelId)
            throw new DtoException("Два разных id", "type/two-another-id");
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(modelId, serviceCenterId))
            throw new ResourceNotFoundException("Опыт с идентификатором № "+modelId+" не найдено", "model/not-found");
        return ResponseEntity.ok(service.update(modelId, serviceCenterId, dto));
    }

    @DeleteMapping("/{modelId}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user,@PathVariable Long modelId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(modelId, serviceCenterId))
            throw new ResourceNotFoundException("Опыт с идентификатором № "+modelId+" не найдено", "model/not-found");
        service.delete(modelId);
        return ResponseEntity.ok("model/deleted");
    }
}
