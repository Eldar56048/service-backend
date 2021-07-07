package com.crm.servicebackend.controller;

import com.crm.servicebackend.dto.requestDto.order.OrderAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.order.OrderAddProductDtoRequest;
import com.crm.servicebackend.dto.requestDto.order.OrderAddServiceDtoRequest;
import com.crm.servicebackend.dto.requestDto.order.OrderUpdateCommentDtoRequest;
import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.model.Order;
import com.crm.servicebackend.model.Status;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.service.*;
import com.crm.servicebackend.utils.facade.OrderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
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
    @Autowired
    private ProductService productService;
    @Autowired
    private ServiceModelService serviceModelService;
    @Autowired
    private OrderItemService orderItemService;

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

    @GetMapping("/status")
    public ResponseEntity<?> getAllByStatus(
            @AuthenticationPrincipal User user,
            @RequestParam Status status,
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
            response = service.getAllByStatus(serviceCenterId, status, page-1, size, sortBy, orderBy);
        else
            response = service.getAllByStatusAndFilter(serviceCenterId, status,page-1, size, sortBy, orderBy, title);
        return ResponseEntity.ok(response);
    }


    @PutMapping
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @Valid @RequestBody OrderAddDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдена", "service-center/not-found");
        if (!modelService.existsByIdAndServiceCenterId(dto.getModel_id(), serviceCenterId))
            throw new ResourceNotFoundException("Модель с идентификатором № "+dto.getModel_id()+" не найдено", "model/not-found");
        if (!typeService.existsByIdAndServiceCenterId(dto.getType_id(), serviceCenterId))
            throw new ResourceNotFoundException("Тип устройства с идентификатором № "+dto.getType_id()+" не найдено", "type/not-found");
        if (!discountService.existsByIdAndServiceCenterId(dto.getDiscount_id(), serviceCenterId))
            throw new ResourceNotFoundException("Скидка с идентификатором № "+dto.getDiscount_id()+" не найдено", "discount/not-found");
        return ResponseEntity.ok(service.add(serviceCenterId, user, dto));
    }

    @GetMapping("/net-profit")
    public ResponseEntity<?> getProfit(
            @AuthenticationPrincipal User user
    ) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        return ResponseEntity.ok(service.getProfit(serviceCenterId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId
    ) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException("Заказ с идентификатором № "+orderId+" не найдено", "order/not-found");
        return ResponseEntity.ok(OrderFacade.modelToOrderDtoResponse(service.get(orderId, serviceCenterId)));
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(@AuthenticationPrincipal User user, @PathVariable Long orderId, @Valid @RequestBody OrderAddDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException("Заказ с идентификатором № "+serviceCenterId+" не найдено", "order/not-found");
        return ResponseEntity.ok((service.update(orderId, serviceCenterId, dto)));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getOrdersCount(@AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        return ResponseEntity.ok(service.getOrdersCount(serviceCenterId));
    }

    @PostMapping("/{orderId}/add/product")
    public ResponseEntity<?> addProductToOrderPost(@AuthenticationPrincipal User user, @PathVariable long orderId, @Valid @RequestBody OrderAddProductDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException("Заказ с идентификатором № "+serviceCenterId+" не найдено", "order/not-found");
        if (!productService.existsByIdAndServiceCenterId(dto.getProduct_id(), serviceCenterId))
            throw new ResourceNotFoundException("Товар с идентификатором № "+dto.getProduct_id()+" не найдено", "product/not-found");
        return ResponseEntity.ok(service.addProductToOrder(orderId, dto, serviceCenterId, user));
    }

    @PostMapping("/{orderId}/add/service")
    public ResponseEntity<?> addServiceToOrder(@AuthenticationPrincipal User user, @PathVariable long orderId,@Valid @RequestBody OrderAddServiceDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException("Заказ с идентификатором № "+serviceCenterId+" не найдено", "order/not-found");
        if (!serviceModelService.existsByIdAndServiceCenterId(dto.getService_id(), serviceCenterId))
            throw new ResourceNotFoundException("Услуга с идентификатором № "+dto.getService_id()+" не найдено", "service/not-found");
        return ResponseEntity.ok(service.addServiceToOrder(orderId, serviceCenterId, dto, user));
    }

    @GetMapping("/{orderId}/done")
    public ResponseEntity<?> orderDone(@AuthenticationPrincipal User user, @PathVariable long orderId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException("Заказ с идентификатором № "+serviceCenterId+" не найдено", "order/not-found");
        return ResponseEntity.ok(service.doneOrder(orderId, serviceCenterId, user));
    }

    @PostMapping("/{orderId}/comment")
    public ResponseEntity<?> orderCommentUpdate(@AuthenticationPrincipal User user, @PathVariable long orderId, @Valid @RequestBody OrderUpdateCommentDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException("Заказ с идентификатором № "+serviceCenterId+" не найдено", "order/not-found");
        return ResponseEntity.ok(service.updateComment(orderId, serviceCenterId, dto));
    }

    @GetMapping("/{orderId}/notify")
    public ResponseEntity<?> notify(@AuthenticationPrincipal User user, @PathVariable long orderId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException("Заказ с идентификатором № "+serviceCenterId+" не найдено", "order/not-found");
        return ResponseEntity.ok(service.notify(orderId, serviceCenterId));
    }

    @GetMapping("/{orderId}/cashier")
    public ResponseEntity<?> orderGiven(@AuthenticationPrincipal User user, @PathVariable long orderId){
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException("Заказ с идентификатором № "+serviceCenterId+" не найдено", "order/not-found");
        return ResponseEntity.ok(service.orderGiven(orderId, serviceCenterId,user));
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> orderRemove(@PathVariable long orderId, @AuthenticationPrincipal User user){
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException("Заказ с идентификатором № "+serviceCenterId+" не найдено", "order/not-found");
        service.removeOrder(orderId, serviceCenterId);
        return ResponseEntity.status(HttpStatus.OK).body("order/successfully-deleted");
    }

    @DeleteMapping("/{orderId}/{item_id}")
    public ResponseEntity<?> orderItemsRemoveFromOrder(@PathVariable long orderId, @PathVariable long item_id, @AuthenticationPrincipal User user){
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException("Заказ с идентификатором № "+serviceCenterId+" не найдено", "order/not-found");
        if(!orderItemService.existsByIdAndServiceCenterIdAndOrderId(item_id,serviceCenterId, orderId))
            throw new ResourceNotFoundException("Элемент заказа с идентификатором № "+item_id+" не найдено", "order-item/not-found");
        Order order = service.get(orderId, serviceCenterId);
        service.deleteOrderItemFromOrder(order, item_id);
        return ResponseEntity.status(HttpStatus.OK).body("order/order-item/successfully-deleted");
    }

    @GetMapping("/{orderId}/payment")
    public ResponseEntity<?> payment(@PathVariable Long orderId, @AuthenticationPrincipal User user, @RequestParam String type){
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException("Заказ с идентификатором № "+serviceCenterId+" не найдено", "order/not-found");
        return ResponseEntity.ok(service.setPaymentType(orderId, serviceCenterId,type));
    }

    @PostMapping("/{orderId}/payment")
    public ResponseEntity<?> updatePaymentType(@AuthenticationPrincipal User user, @PathVariable  long orderId,@RequestParam String payment){
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException("Заказ с идентификатором № "+serviceCenterId+" не найдено", "order/not-found");
        return ResponseEntity.ok(service.updatePaymentType(orderId, serviceCenterId,payment));
    }
}
