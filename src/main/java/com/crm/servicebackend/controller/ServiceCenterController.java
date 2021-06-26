package com.crm.servicebackend.controller;

import com.crm.servicebackend.dto.requestDto.serviceCenter.ServiceCenterAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.serviceCenter.ServiceCenterUpdateDtoRequest;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.service.ServiceCenterService;
import com.crm.servicebackend.utils.facade.ServiceCenterFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/service-centers")
public class ServiceCenterController {

    private final ServiceCenterService service;

    @Autowired
    public ServiceCenterController(ServiceCenterService service) {
        this.service = service;
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
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+id+" не найдена", "service-center/not-found");
        return ResponseEntity.ok(ServiceCenterFacade.modelToDtoResponse(service.get(id)));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ServiceCenterUpdateDtoRequest dto) {
        if (dto.getId()!=id)
            throw new DtoException("Два разных id", "service-center/two-another-id");
        if (!service.existsById(id))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+id+" не найдена", "service-center/not-found");
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!service.existsById(id))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+id+" не найдена", "service-center/not-found");
        service.delete(id);
        return ResponseEntity.ok("service-center/deleted");
    }
}
