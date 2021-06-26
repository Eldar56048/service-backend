package com.crm.servicebackend.controller;

import com.crm.servicebackend.dto.requestDto.discount.DiscountAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.discount.DiscountUpdateDtoRequest;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.service.DiscountService;
import com.crm.servicebackend.service.ServiceCenterService;
import com.crm.servicebackend.utils.facade.DiscountFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/discounts")
public class DiscountController {
    private final DiscountService service;
    private final ServiceCenterService serviceCenterService;

    @Autowired
    public DiscountController(DiscountService service, ServiceCenterService serviceCenterService) {
        this.service = service;
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
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдена", "service-center/not-found");
        if (title.length()<=0)
            response = service.getAll(serviceCenterId,page-1, size, sortBy, orderBy);
        else
            response = service.getAllAndFilter(serviceCenterId,page-1, size, sortBy, orderBy, title);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> add(@RequestParam Long serviceCenterId, @Valid @RequestBody DiscountAddDtoRequest dto) {
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдена", "service-center/not-found");
        return ResponseEntity.ok(service.add(serviceCenterId, dto));
    }

    @GetMapping("/{discountId}")
    public ResponseEntity<?> get(@RequestParam Long serviceCenterId, @PathVariable Long discountId) {
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдена", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(discountId, serviceCenterId))
            throw new ResourceNotFoundException("Скидка с идентификатором № "+discountId+" не найдена", "discount/not-found");
        return ResponseEntity.ok(DiscountFacade.modelToDtoResponse(service.get(discountId, serviceCenterId)));
    }

    @PostMapping("/{discountId}")
    public ResponseEntity<?> update(@RequestParam Long serviceCenterId, @PathVariable Long discountId, @Valid @RequestBody DiscountUpdateDtoRequest dto) {
        if (dto.getId()!=discountId)
            throw new DtoException("Два разных id", "service-center/two-another-id");
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдена", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(discountId, serviceCenterId))
            throw new ResourceNotFoundException("Скидка с идентификатором № "+discountId+" не найдена", "discount/not-found");
        return ResponseEntity.ok(service.update(discountId, serviceCenterId, dto));
    }

    @DeleteMapping("/{discountId}")
    public ResponseEntity<?> delete(@RequestParam Long serviceCenterId,@PathVariable Long discountId) {
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдена", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(discountId, serviceCenterId))
            throw new ResourceNotFoundException("Скидка с идентификатором № "+discountId+" не найдена", "discount/not-found");
        service.delete(discountId);
        return ResponseEntity.ok("discount/deleted");
    }
}
