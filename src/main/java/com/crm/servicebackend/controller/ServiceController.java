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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

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
    public ResponseEntity<?> getAllForSelect(@AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        return ResponseEntity.ok(service.getServicesForSelect(serviceCenterId));
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
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @Valid @RequestBody ServiceAddDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        return ResponseEntity.ok(service.add(serviceCenterId, dto));
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<?> get(@AuthenticationPrincipal User user, @PathVariable Long serviceId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(serviceId, serviceCenterId))
            throw new ResourceNotFoundException("Услуга с идентификатором № "+serviceId+" не найдено", "service/not-found");
        return ResponseEntity.ok(ServiceFacade.modelToDtoResponse(service.get(serviceId, serviceCenterId)));
    }

    @GetMapping("/{serviceId}/sold")
    public ResponseEntity<?> getServiceSoldCount(@AuthenticationPrincipal User user,@PathVariable long serviceId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(serviceId, serviceCenterId))
            throw new ResourceNotFoundException("Услуга с идентификатором № "+serviceId+" не найдено", "service/not-found");
        return ResponseEntity.ok(service.getServiceSoldCount(serviceCenterId, serviceId).getSoldCount());
    }

    @GetMapping("/{serviceId}/monthly/sold")
    public ResponseEntity<?> getMonthlySold(@PathVariable long serviceId, @AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(serviceId, serviceCenterId))
            throw new ResourceNotFoundException("Услуга с идентификатором № "+serviceId+" не найдено", "service/not-found");
        return ResponseEntity.ok(service.getMonthlySold(serviceCenterId, serviceId));
    }

    @GetMapping("/top-profit-services")
    public ResponseEntity<?> getTopProfitServices(@AuthenticationPrincipal User user, @RequestParam(defaultValue = "5") int limit) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        return ResponseEntity.ok(service.getTopProfitServices(serviceCenterId, limit));
    }

    @GetMapping("/top-sale")
    public ResponseEntity<?> getTopSale(@AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        return ResponseEntity.ok(service.getTopSaleService(serviceCenterId));
    }

    @GetMapping("/sold-count")
    public ResponseEntity<?> getSoldServiceCount(@AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        return ResponseEntity.ok(service.getSoldServiceCount(serviceCenterId));
    }

    @PostMapping("/{serviceId}")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user, @PathVariable Long serviceId, @Valid @RequestBody ServiceUpdateDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if (dto.getId()!=serviceId)
            throw new DtoException("Два разных id", "service/two-another-id");
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(serviceId, serviceCenterId))
            throw new ResourceNotFoundException("Услуга с идентификатором № "+serviceId+" не найдено", "service/not-found");
        return ResponseEntity.ok(service.update(serviceId, serviceCenterId, dto, user));
    }

    @DeleteMapping("/{serviceId}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user,@PathVariable Long serviceId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(serviceId, serviceCenterId))
            throw new ResourceNotFoundException("Услуга с идентификатором № "+serviceId+" не найдено", "service/not-found");
        service.delete(serviceId, serviceCenterId);
        return ResponseEntity.ok("service/deleted");
    }

}
