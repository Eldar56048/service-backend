package com.crm.servicebackend.controller;

import com.crm.servicebackend.dto.requestDto.client.ClientAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.client.ClientUpdateDtoRequest;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.service.ClientService;
import com.crm.servicebackend.service.DiscountService;
import com.crm.servicebackend.service.OrderService;
import com.crm.servicebackend.service.ServiceCenterService;
import com.crm.servicebackend.utils.facade.ClientFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.crm.servicebackend.constant.model.client.ClientResponseCode.*;
import static com.crm.servicebackend.constant.model.client.ClientResponseMessage.CLIENT_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.client.ClientResponseMessage.CLIENT_TWO_ANOTHER_ID_MESSAGE;
import static com.crm.servicebackend.constant.model.discount.DiscountResponseCode.DISCOUNT_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.discount.DiscountResponseMessage.DISCOUNT_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseCode.SERVICE_CENTER_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseMessage.SERVICE_CENTER_NOT_FOUND_MESSAGE;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService service;
    private final DiscountService discountService;
    private final ServiceCenterService serviceCenterService;

    @Autowired
    private OrderService orderService;

    @Autowired
    public ClientController(ClientService service, DiscountService discountService, ServiceCenterService serviceCenterService) {
        this.service = service;
        this.discountService = discountService;
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
        Long serviceCenterId = user.getServiceCenter().getId();
        Map<String, Object> response;
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
    public ResponseEntity<?> getAllForSelect(
            @AuthenticationPrincipal User user
    ) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
       return ResponseEntity.ok(service.getAllForSelect(serviceCenterId));
    }

    @GetMapping("/{clientId}/orders")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> getAllClientOrders(
            @AuthenticationPrincipal User user,
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String orderBy,
            @RequestParam(defaultValue = "") String title
    ) {
        Long serviceCenterId = user.getServiceCenter().getId();
        Map<String, Object> response;
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(clientId, serviceCenterId))
            throw new ResourceNotFoundException(CLIENT_NOT_FOUND_MESSAGE(clientId), CLIENT_NOT_FOUND_CODE);
        if (title.length()<=0)
            response = orderService.getAllByClient(serviceCenterId,clientId, page-1, size, sortBy, orderBy);
        else
            response = orderService.getAllByClientAndFilter(serviceCenterId, clientId,page-1, size, sortBy, orderBy, title);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @Valid @RequestBody ClientAddDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!discountService.existsByIdAndServiceCenterId(dto.getDiscountId(), serviceCenterId))
            throw new ResourceNotFoundException(DISCOUNT_NOT_FOUND_MESSAGE(dto.getDiscountId()), DISCOUNT_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.add(serviceCenterId, dto));
    }

    @GetMapping("/{clientId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> get(@AuthenticationPrincipal User user, @PathVariable Long clientId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(clientId, serviceCenterId))
            throw new ResourceNotFoundException(CLIENT_NOT_FOUND_MESSAGE(clientId), CLIENT_NOT_FOUND_CODE);
        return ResponseEntity.ok(ClientFacade.modelToDtoResponse(service.get(clientId, serviceCenterId)));
    }

    @PostMapping("/{clientId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user, @PathVariable Long clientId, @Valid @RequestBody ClientUpdateDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if (dto.getId()!=clientId)
            throw new DtoException(CLIENT_TWO_ANOTHER_ID_MESSAGE, CLIENT_TWO_ANOTHER_ID_CODE);
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!discountService.existsByIdAndServiceCenterId(dto.getDiscountId(), serviceCenterId))
            throw new ResourceNotFoundException(DISCOUNT_NOT_FOUND_MESSAGE(dto.getDiscountId()), DISCOUNT_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(clientId, serviceCenterId))
            throw new ResourceNotFoundException(CLIENT_NOT_FOUND_MESSAGE(clientId), CLIENT_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.update(clientId, serviceCenterId, dto));
    }

    @DeleteMapping("/{clientId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> delete(@AuthenticationPrincipal User user, @PathVariable Long clientId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!service.existsByIdAndServiceCenterId(clientId, serviceCenterId))
            throw new ResourceNotFoundException(CLIENT_NOT_FOUND_MESSAGE(clientId), CLIENT_NOT_FOUND_CODE);
        service.delete(clientId);
        return ResponseEntity.ok(CLIENT_DELETED_CODE);
    }
}
