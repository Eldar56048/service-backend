package com.crm.servicebackend.controller;

import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService service;
    @Autowired
    private ServiceCenterService serviceCenterService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private DiscountService discountService;

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

    /*@PutMapping
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @Valid @RequestBody UserAddDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдена", "service-center/not-found");
        if (!modelService.existsByIdAndServiceCenterId(dto.getExperienceModelId(), serviceCenterId))
            throw new ResourceNotFoundException("Опыт с идентификатором № "+dto.getExperienceModelId()+" не найдено", "experience-model/not-found");
        return ResponseEntity.ok(service.add(serviceCenterId, dto, user));
    }*/

}
