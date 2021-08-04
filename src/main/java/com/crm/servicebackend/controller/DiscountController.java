package com.crm.servicebackend.controller;

import com.crm.servicebackend.dto.requestDto.discount.DiscountAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.discount.DiscountUpdateDtoRequest;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.service.DiscountService;
import com.crm.servicebackend.service.ServiceCenterService;
import com.crm.servicebackend.utils.facade.DiscountFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.crm.servicebackend.constant.model.discount.DiscountResponseCode.*;
import static com.crm.servicebackend.constant.model.discount.DiscountResponseMessage.DISCOUNT_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.discount.DiscountResponseMessage.DISCOUNT_TWO_ANOTHER_ID_MESSAGE;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseCode.SERVICE_CENTER_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseMessage.SERVICE_CENTER_NOT_FOUND_MESSAGE;


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
    @PreAuthorize("hasAuthority('USER')")
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
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getAllForSelect(@AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.getAllForSelect(serviceCenterId));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @Valid @RequestBody DiscountAddDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.add(serviceCenterId, dto));
    }

    @GetMapping("/{discountId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> get(@AuthenticationPrincipal User user, @PathVariable Long discountId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(discountId, serviceCenterId))
            throw new ResourceNotFoundException(DISCOUNT_NOT_FOUND_MESSAGE(discountId), DISCOUNT_NOT_FOUND_CODE);
        return ResponseEntity.ok(DiscountFacade.modelToDtoResponse(service.get(discountId, serviceCenterId)));
    }

    @PostMapping("/{discountId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user, @PathVariable Long discountId, @Valid @RequestBody DiscountUpdateDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if (dto.getId()!=discountId)
            throw new DtoException(DISCOUNT_TWO_ANOTHER_ID_MESSAGE, DISCOUNT_TWO_ANOTHER_ID_CODE);
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(discountId, serviceCenterId))
            throw new ResourceNotFoundException(DISCOUNT_NOT_FOUND_MESSAGE(discountId), DISCOUNT_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.update(discountId, serviceCenterId, dto));
    }

    @DeleteMapping("/{discountId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user,@PathVariable Long discountId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(discountId, serviceCenterId))
            throw new ResourceNotFoundException(DISCOUNT_NOT_FOUND_MESSAGE(discountId), DISCOUNT_NOT_FOUND_CODE);
        service.delete(discountId);
        return ResponseEntity.ok(DISCOUNT_DELETED_CODE);
    }
}
