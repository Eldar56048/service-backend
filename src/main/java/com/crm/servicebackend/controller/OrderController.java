package com.crm.servicebackend.controller;

import com.crm.servicebackend.dto.requestDto.order.OrderAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.order.OrderAddProductDtoRequest;
import com.crm.servicebackend.dto.requestDto.order.OrderAddServiceDtoRequest;
import com.crm.servicebackend.dto.requestDto.order.OrderUpdateCommentDtoRequest;
import com.crm.servicebackend.dto.requestDto.report.AcceptedOrdersReportDtoRequest;
import com.crm.servicebackend.dto.requestDto.report.CalculateSalaryRequestDto;
import com.crm.servicebackend.dto.requestDto.report.DoneOrdersReportDtoRequest;
import com.crm.servicebackend.dto.requestDto.report.GivenOrdersReportDtoRequest;
import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.model.Order;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.model.enums.Status;
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

import static com.crm.servicebackend.constant.model.discount.DiscountResponseCode.DISCOUNT_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.discount.DiscountResponseMessage.DISCOUNT_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.model.ModelResponseCode.MODEL_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.model.ModelResponseMessage.MODEL_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.order.OrderResponseCode.ORDER_DELETED_CODE;
import static com.crm.servicebackend.constant.model.order.OrderResponseCode.ORDER_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.order.OrderResponseMessage.ORDER_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.orderItem.OrderItemResponseCode.ORDER_ITEM_DELETED_CODE;
import static com.crm.servicebackend.constant.model.orderItem.OrderItemResponseCode.ORDER_ITEM_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.orderItem.OrderItemResponseMessage.ORDER_ITEM_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.product.ProductResponseCode.PRODUCT_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.product.ProductResponseMessage.PRODUCT_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.service.ServiceResponseCode.SERVICE_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.service.ServiceResponseMessage.SERVICE_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseCode.SERVICE_CENTER_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.serviceCenter.ServiceCenterResponseMessage.SERVICE_CENTER_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.type.TypeResponseCode.TYPE_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.type.TypeResponseMessage.TYPE_NOT_FOUND_MESSAGE;
import static com.crm.servicebackend.constant.model.user.UserResponseCode.USER_NOT_FOUND_CODE;
import static com.crm.servicebackend.constant.model.user.UserResponseMessage.USER_NOT_FOUND_MESSAGE;

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
    @Autowired
    private UserService userService;

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
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
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
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
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
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if (!modelService.existsByIdAndServiceCenterId(dto.getModel_id(), serviceCenterId))
            throw new ResourceNotFoundException(MODEL_NOT_FOUND_MESSAGE(dto.getModel_id()), MODEL_NOT_FOUND_CODE);
        if (!typeService.existsByIdAndServiceCenterId(dto.getType_id(), serviceCenterId))
            throw new ResourceNotFoundException(TYPE_NOT_FOUND_MESSAGE(dto.getType_id()), TYPE_NOT_FOUND_CODE);
        if (!discountService.existsByIdAndServiceCenterId(dto.getDiscount_id(), serviceCenterId))
            throw new ResourceNotFoundException(DISCOUNT_NOT_FOUND_MESSAGE(dto.getDiscount_id()), DISCOUNT_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.add(serviceCenterId, user, dto));
    }

    @GetMapping("/net-profit")
    public ResponseEntity<?> getProfit(
            @AuthenticationPrincipal User user
    ) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.getProfit(serviceCenterId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(
            @AuthenticationPrincipal User user,
            @PathVariable Long orderId
    ) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException(ORDER_NOT_FOUND_MESSAGE(orderId), ORDER_NOT_FOUND_CODE);
        return ResponseEntity.ok(OrderFacade.modelToOrderDtoResponse(service.get(orderId, serviceCenterId)));
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(@AuthenticationPrincipal User user, @PathVariable Long orderId, @Valid @RequestBody OrderAddDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException(ORDER_NOT_FOUND_MESSAGE(orderId), ORDER_NOT_FOUND_CODE);
        return ResponseEntity.ok((service.update(orderId, serviceCenterId, dto)));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getOrdersCount(@AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.getOrdersCount(serviceCenterId));
    }

    @PostMapping("/{orderId}/add/product")
    public ResponseEntity<?> addProductToOrderPost(@AuthenticationPrincipal User user, @PathVariable long orderId, @Valid @RequestBody OrderAddProductDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException(ORDER_NOT_FOUND_MESSAGE(orderId), ORDER_NOT_FOUND_CODE);
        if (!productService.existsByIdAndServiceCenterId(dto.getProduct_id(), serviceCenterId))
            throw new ResourceNotFoundException(PRODUCT_NOT_FOUND_MESSAGE(dto.getProduct_id()), PRODUCT_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.addProductToOrder(orderId, dto, serviceCenterId, user));
    }

    @PostMapping("/{orderId}/add/service")
    public ResponseEntity<?> addServiceToOrder(@AuthenticationPrincipal User user, @PathVariable long orderId,@Valid @RequestBody OrderAddServiceDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException(ORDER_NOT_FOUND_MESSAGE(orderId), ORDER_NOT_FOUND_CODE);
        if (!serviceModelService.existsByIdAndServiceCenterId(dto.getService_id(), serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_NOT_FOUND_MESSAGE(dto.getService_id()), SERVICE_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.addServiceToOrder(orderId, serviceCenterId, dto, user));
    }

    @GetMapping("/{orderId}/done")
    public ResponseEntity<?> orderDone(@AuthenticationPrincipal User user, @PathVariable long orderId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException(ORDER_NOT_FOUND_MESSAGE(orderId), ORDER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.doneOrder(orderId, serviceCenterId, user));
    }

    @PostMapping("/{orderId}/comment")
    public ResponseEntity<?> orderCommentUpdate(@AuthenticationPrincipal User user, @PathVariable long orderId, @Valid @RequestBody OrderUpdateCommentDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException(ORDER_NOT_FOUND_MESSAGE(orderId), ORDER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.updateComment(orderId, serviceCenterId, dto));
    }

    @GetMapping("/{orderId}/notify")
    public ResponseEntity<?> notify(@AuthenticationPrincipal User user, @PathVariable long orderId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException(ORDER_NOT_FOUND_MESSAGE(orderId), ORDER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.notify(orderId, serviceCenterId));
    }

    @GetMapping("/{orderId}/cashier")
    public ResponseEntity<?> orderGiven(@AuthenticationPrincipal User user, @PathVariable long orderId){
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException(ORDER_NOT_FOUND_MESSAGE(orderId), ORDER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.orderGiven(orderId, serviceCenterId,user));
    }

    @PreAuthorize("hasAuthority('MODERATOR')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> orderRemove(@PathVariable long orderId, @AuthenticationPrincipal User user){
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException(ORDER_NOT_FOUND_MESSAGE(orderId), ORDER_NOT_FOUND_CODE);
        service.removeOrder(orderId, serviceCenterId);
        return ResponseEntity.status(HttpStatus.OK).body(ORDER_DELETED_CODE);
    }

    @DeleteMapping("/{orderId}/{item_id}")
    public ResponseEntity<?> orderItemsRemoveFromOrder(@PathVariable long orderId, @PathVariable long item_id, @AuthenticationPrincipal User user){
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException(ORDER_NOT_FOUND_MESSAGE(orderId), ORDER_NOT_FOUND_CODE);
        if(!orderItemService.existsByIdAndServiceCenterIdAndOrderId(item_id,serviceCenterId, orderId))
            throw new ResourceNotFoundException(ORDER_ITEM_NOT_FOUND_MESSAGE(item_id), ORDER_ITEM_NOT_FOUND_CODE);
        Order order = service.get(orderId, serviceCenterId);
        service.deleteOrderItemFromOrder(order, item_id);
        return ResponseEntity.status(HttpStatus.OK).body(ORDER_ITEM_DELETED_CODE);
    }

    @GetMapping("/{orderId}/payment")
    public ResponseEntity<?> payment(@PathVariable Long orderId, @AuthenticationPrincipal User user, @RequestParam String type){
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException(ORDER_NOT_FOUND_MESSAGE(orderId), ORDER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.setPaymentType(orderId, serviceCenterId,type));
    }

    @PostMapping("/{orderId}/payment")
    public ResponseEntity<?> updatePaymentType(@AuthenticationPrincipal User user, @PathVariable  long orderId,@RequestParam String payment){
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if(!service.existsByIdAndServiceCenterId(orderId, serviceCenterId))
            throw new ResourceNotFoundException(ORDER_NOT_FOUND_MESSAGE(orderId), ORDER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.updatePaymentType(orderId, serviceCenterId,payment));
    }

    @GetMapping("/calculate-salary")
    public ResponseEntity<?> calculateMasterSalary(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "1") int size,
                                                   @RequestParam(defaultValue = "id") String sortBy,
                                                   @RequestParam(defaultValue = "desc") String orderBy,
                                                   @Valid CalculateSalaryRequestDto dto,
                                                   @AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        if(!userService.existsByIdAndServiceCenterId(dto.getUserId(), serviceCenterId)){
            throw new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE(dto.getUserId()), USER_NOT_FOUND_CODE);
        }
        return ResponseEntity.ok(service.calculateMasterSalary(serviceCenterId, dto, page-1, size, sortBy, orderBy));
    }

    @GetMapping("/report-accepted")
    public ResponseEntity<?> acceptedOrdersReport(@RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "1") int size,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "desc") String orderBy,
                                                  @Valid AcceptedOrdersReportDtoRequest dto,
                                                  @AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.acceptedOrdersReport(serviceCenterId, dto, page-1, size, sortBy, orderBy));
    }

    @GetMapping("/report-done")
    public ResponseEntity<?> doneOrdersReport(@RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "1") int size,
                                              @RequestParam(defaultValue = "id") String sortBy,
                                              @RequestParam(defaultValue = "desc") String orderBy,
                                              @Valid DoneOrdersReportDtoRequest dto,
                                              @AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.doneOrdersReport(serviceCenterId, dto, page-1, size, sortBy, orderBy));
    }

    @GetMapping("/report-given")
    public ResponseEntity<?> givenOrdersReport(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "1") int size,
                                               @RequestParam(defaultValue = "id") String sortBy,
                                               @RequestParam(defaultValue = "desc") String orderBy,
                                               @Valid GivenOrdersReportDtoRequest dto,
                                               @AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        System.out.println(sortBy+" ______________________________");
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException(SERVICE_CENTER_NOT_FOUND_MESSAGE(serviceCenterId), SERVICE_CENTER_NOT_FOUND_CODE);
        return ResponseEntity.ok(service.givenOrdersReport(serviceCenterId, dto, page-1, size, sortBy, orderBy));
    }
}
