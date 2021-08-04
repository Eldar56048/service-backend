package com.crm.servicebackend.controller;

import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.service.OrderService;
import com.crm.servicebackend.utils.facade.OrderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.crm.servicebackend.constant.model.order.OrderResponseCode.ORDER_NOT_FOUND_OR_INVALID_PASSWORD_CODE;
import static com.crm.servicebackend.constant.model.order.OrderResponseMessage.ORDER_NOT_FOUND_OR_INVALID_PASSWORD_MESSAGE;


@RestController
@RequestMapping("/api/v1/client")
public class CustomerController {
    private final OrderService orderService;

    @Autowired
    public CustomerController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/order/{id}/{token}")
    public ResponseEntity<?> getOrder(@PathVariable Long id, @PathVariable String token) {
        if(!orderService.existsByIdAndToken(id, token))
            throw new ResourceNotFoundException(ORDER_NOT_FOUND_OR_INVALID_PASSWORD_MESSAGE(id), ORDER_NOT_FOUND_OR_INVALID_PASSWORD_CODE);
        return ResponseEntity.ok(OrderFacade.modelToOrderDtoResponse(orderService.getById(id)));
    }
}
