package com.crm.servicebackend.service;

import com.crm.servicebackend.dto.requestDto.order.OrderAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.order.OrderAddProductDtoRequest;
import com.crm.servicebackend.dto.requestDto.order.OrderAddServiceDtoRequest;
import com.crm.servicebackend.dto.requestDto.order.OrderUpdateCommentDtoRequest;
import com.crm.servicebackend.dto.responseDto.order.OrderDtoResponse;
import com.crm.servicebackend.dto.responseDto.order.OrderForListDtoResponse;
import com.crm.servicebackend.dto.responseDto.orderItem.OrderItemOrderDtoResponse;
import com.crm.servicebackend.dto.responseDto.statistics.Count;
import com.crm.servicebackend.dto.responseDto.statistics.NetProfit;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.model.*;
import com.crm.servicebackend.repository.OrderRepository;
import com.crm.servicebackend.utils.RandomGenerator;
import com.crm.servicebackend.utils.facade.OrderFacade;
import com.crm.servicebackend.utils.facade.OrderItemFacade;
import com.crm.servicebackend.utils.facade.PaginationResponseFacade;
import com.crm.servicebackend.utils.smsc.Smsc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.Map;

import static com.crm.servicebackend.constant.ConstantVariables.*;
import static com.crm.servicebackend.constant.response.order.OrderResponseCode.*;
import static com.crm.servicebackend.constant.response.order.OrderResponseMessage.*;
import static com.crm.servicebackend.constant.response.storage.StorageResponseCode.PRODUCT_STORAGE_NOT_ENOUGH_CODE;
import static com.crm.servicebackend.constant.response.storage.StorageResponseMessage.PRODUCT_STORAGE_NOT_ENOUGH_MESSAGE;

@org.springframework.stereotype.Service
public class OrderService {
    @Autowired
    private  OrderRepository repository;
    @Autowired
    private  ServiceCenterService serviceCenterService;
    @Autowired
    private  DiscountService discountService;
    @Autowired
    private  ClientService clientService;
    @Autowired
    private  TypeService typeService;
    @Autowired
    private  ModelService modelService;
    @Autowired
    private  ProductService productService;
    @Autowired
    private  ReceivingHistoryService receivingHistoryService;
    @Autowired
    private  IncomingHistoryService incomingHistoryService;
    @Autowired
    private  StorageService storageService;
    @Autowired
    private  OrderItemService orderItemService;
    @Autowired
    private  ServiceModelService serviceModelService;
    @Autowired
    private  Smsc smsc;

    public Map<String, Object> getAll(Long serviceCenterId, int page, int size, String sortBy, String orderBy){
        Pageable pageable;
        if (orderBy.equals("asc"))
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return PaginationResponseFacade.response(pageToDtoPage(repository.findAllByServiceCenterId(serviceCenterId, pageable)));
    }

    public Map<String, Object> getAllAndFilter(Long serviceCenterId, int page, int size, String sortBy, String orderBy, String title){
        Pageable pageable;
        if (orderBy.equals("asc"))
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return PaginationResponseFacade.response(pageToDtoPage(repository.findAllByServiceCenterIdAndFilter(serviceCenterId, title, pageable)));
    }

    public Map<String, Object> getAllByClient(Long serviceCenterId, Long clientId, int page, int size, String sortBy, String orderBy){
        Pageable pageable;
        if (orderBy.equals("asc"))
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return PaginationResponseFacade.response(pageToDtoPage(repository.findAllByServiceCenterIdAndClientId(serviceCenterId,clientId, pageable)));
    }

    public Map<String, Object> getAllByStatus(Long serviceCenterId, Status status, int page, int size, String sortBy, String orderBy){
        Pageable pageable;
        if (orderBy.equals("asc"))
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return PaginationResponseFacade.response(pageToDtoPage(repository.findAllByServiceCenterIdAndStatus(serviceCenterId,status, pageable)));
    }

    public Map<String, Object> getAllByStatusAndFilter(Long serviceCenterId, Status status, int page, int size, String sortBy, String orderBy, String title){
        Pageable pageable;
        if (orderBy.equals("asc"))
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return PaginationResponseFacade.response(pageToDtoPage(repository.findAllByServiceCenterIdAndStatusAndFilter(serviceCenterId, status, title, pageable)));
    }

    public Map<String, Object> getAllByClientAndFilter(Long serviceCenterId, Long clientId, int page, int size, String sortBy, String orderBy, String title){
        Pageable pageable;
        if (orderBy.equals("asc"))
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return PaginationResponseFacade.response(pageToDtoPage(repository.findAllByServiceCenterIdAndClientAndFilter(serviceCenterId,clientId, title, pageable)));
    }

    public OrderForListDtoResponse add(Long serviceCenterId, User user, OrderAddDtoRequest dto){
        Type type = typeService.get(dto.getType_id(), serviceCenterId);
        Model model = modelService.get(dto.getModel_id(), serviceCenterId);
        Discount discount = discountService.get(dto.getDiscount_id(), serviceCenterId);
        ServiceCenter serviceCenter = discount.getServiceCenter();
        Order order = null;
        Client client = null;
        if((discount.getDiscountName().equals(STANDARD_DISCOUNT_NAME)==false)&&(onlyDigits(dto.getClientId(),dto.getClientId().length())==false)){
            String surname = "";
            String name = "";
            String[] words = dto.getClientId().split(" ");
            if (words.length>1) {
                surname = words[0];
                name = words[1];
            }
            else{
                name = words[0];
                surname = DEFAULT_CLIENT_SURNAME;
            }
            client = new Client(name,surname,dto.getClient_number(),discount);
            client.setServiceCenter(serviceCenter);
            clientService.save(client);
            order = new Order(client,dto.getProblem(),user,type,model,dto.getModelType());
        }
        else if(onlyDigits(dto.getClientId(),dto.getClientId().length())){
            client = clientService.get(Long.parseLong(dto.getClientId()), serviceCenterId);
            client.setDiscount(discount);
            client.setPhoneNumber(dto.getClient_number());
            clientService.save(client);
            order = new Order(client,dto.getProblem(),user,type,model,dto.getModelType());
        }
        else {
            order = new Order(dto.getClientId(), dto.getClient_number(), dto.getProblem(), user, type, model,dto.getModelType(),discount.getDiscountName(),discount.getPercentage());
        }
        order.setDiscount(discount);
        order.setNotified(false);
        order.setToken(RandomGenerator.generate(8));
        order.setServiceCenter(serviceCenterService.get(serviceCenterId));
        order = save(order);
        return modelToOrderForListDtoResponse(order);
    }

    public OrderDtoResponse update(long orderId, Long serviceCenterId,OrderAddDtoRequest dto){
        Order order = get(orderId,serviceCenterId);
        Discount discount = discountService.get(dto.getDiscount_id(), serviceCenterId);
        Client client = null;
        ServiceCenter serviceCenter = order.getServiceCenter();
        if((discount.getDiscountName().equals(STANDARD_DISCOUNT_NAME)==false)&&(onlyDigits(dto.getClientId(),dto.getClientId().length())==false)){
            String surname = "";
            String name = "";
            String[] words = dto.getClientId().split(" ");
            if (words.length>1) {
                surname = words[0];
                name = words[1];
            }
            else{
                name = words[0];
                surname = DEFAULT_CLIENT_SURNAME;
            }
            client = new Client(name,surname,dto.getClient_number(),discount);
            client.setServiceCenter(serviceCenter);
            clientService.save(client);
            order.setClient(client);
        }
        else if(onlyDigits(dto.getClientId(),dto.getClientId().length())){
            client = clientService.get(Long.parseLong(dto.getClientId()), serviceCenterId);
            client.setDiscount(discount);
            client.setPhoneNumber(dto.getClient_number());
            clientService.save(client);
            order.setClient(client);
            order.setPhoneNumber(dto.getClient_number());
        }
        else {
            order.setClient(null);
            order.setClientName(dto.getClientId());
            order.setPhoneNumber(dto.getClient_number());
        }
        order.setDiscount(discount);
        order.setDiscountName(discount.getDiscountName());
        order.setDiscountPercent(discount.getPercentage());
        order.setType(typeService.get(dto.getType_id(), serviceCenterId));
        order.setModel(modelService.get(dto.getModel_id(), serviceCenterId));
        order.setModelCompany(dto.getModelType());
        order.setProblem(dto.getProblem());
        return modelToDtoResponse(save(order));
    }

    public Count getOrdersCount(Long serviceCenterId) {
        return repository.getOrdersCount(serviceCenterId);
    }

    public OrderItemOrderDtoResponse addProductToOrder(Long orderId, OrderAddProductDtoRequest dto, Long serviceCenterId, User user){
        Order order = get(orderId, serviceCenterId);
        Product product = productService.get(dto.getProduct_id(), serviceCenterId);
        Storage storage = storageService.get(dto.getProduct_id(), serviceCenterId);
        ServiceCenter serviceCenter = order.getServiceCenter();
        if (storage.getQuantity() < dto.getQuantity()) {
            throw new DtoException(PRODUCT_STORAGE_NOT_ENOUGH_MESSAGE((dto.getQuantity()-storage.getQuantity())), PRODUCT_STORAGE_NOT_ENOUGH_CODE);
        }
        OrderItem orderItem = new OrderItem(dto.getQuantity(), order, product,user, (int) productService.getLastPrice(dto.getProduct_id(), serviceCenterId));
        storage.setQuantity(storage.getQuantity() - dto.getQuantity());
        ReceivingHistory receivingHistory = new ReceivingHistory(serviceCenter, orderItem);
        storageService.save(storage);
        orderItem.setServiceCenter(serviceCenter);
        orderItem = orderItemService.save(orderItem);
        order.addOrderItem(orderItem);
        save(order);
        receivingHistoryService.save(receivingHistory);
        return OrderItemFacade.modelToOrdersOrderItemDtoResponse(orderItem);
    }

    public OrderItemOrderDtoResponse addServiceToOrder(Long orderId, Long serviceCenterId, OrderAddServiceDtoRequest model, User user){
        Service service = serviceModelService.get(model.getService_id(), serviceCenterId);
        Order order = get(orderId, serviceCenterId);
        OrderItem orderItem = new OrderItem(model.getQuantity(), order, service,user);
        orderItem.setServiceCenter(order.getServiceCenter());
        orderItem=orderItemService.save(orderItem);
        order.addOrderItem(orderItem);
        save(order);
        return OrderItemFacade.modelToOrdersOrderItemDtoResponse(orderItem);
    }

    public OrderDtoResponse doneOrder(Long orderId, Long serviceCenterId,User user){
        Order order = get(orderId, serviceCenterId);
        if (order.getStatus() == Status.NOTDONE) {
            order.setStatus(Status.DONE);
            order.setDoneUser(user);
            order.setDoneDate(new Date());
        } else {
            throw new DtoException(ORDER_STATUS_NOT_CHANGED_MESSAGE(order.getStatus().toString()), ORDER_STATUS_NOT_CHANGED_CODE);
        }
        return modelToDtoResponse(save(order));
    }

    public OrderDtoResponse updateComment(Long orderId, Long serviceCenterId, OrderUpdateCommentDtoRequest dto){
        Order order = get(orderId, serviceCenterId);
        order.setComment(dto.getComment());
        return modelToDtoResponse(save(order));
    }

    public OrderDtoResponse notify(Long orderId, Long serviceCenterId){
        Order order = get(orderId, serviceCenterId);
        if (order.getNotified()==true) {
            throw new DtoException(ORDER_ALREADY_NOTIFIED_MESSAGE, ORDER_ALREADY_NOTIFIED_CODE);
        }
        String message = ORDER_NOTIFY_MESSAGE(order);
        String phoneNumber = "";
        if (order.getPhoneNumber() != null) {
            phoneNumber = order.getPhoneNumber();
        } else {
            phoneNumber = order.getClient().getPhoneNumber();
        }
        boolean send = smsc.send_sms(phoneNumber, message, 1, "", "", 0, "", "");
        if (send == false) {
            throw new DtoException(ORDER_NOT_NOTIFIED_MESSAGE, ORDER_NOT_NOTIFIED_CODE);
        }
        order.setNotified(send);
        return modelToDtoResponse(save(order));
    }

    public OrderDtoResponse orderGiven(Long orderId, Long serviceCenterId,User user){
        Order order = get(orderId, serviceCenterId);
        if (order.getStatus() == Status.DONE) {
            order.setStatus(Status.WENTCASHIER);
            order.setGiveUser(user);
        } else {
            throw new DtoException(ORDER_STATUS_NOT_CHANGED_MESSAGE(order.getStatus().toString()), ORDER_STATUS_NOT_CHANGED_CODE);
        }
        return modelToDtoResponse(save(order));
    }

    public void removeOrder(Long orderId, Long serviceId){
        Order order = get(orderId, serviceId);
        for(int i=0; i<order.getItems().size();i++){
            deleteOrderItemFromOrder(order, order.getItems().get(i).getId());
        }
        repository.deleteById(order.getId());
    }

    public void deleteOrderItemFromOrder(Order order, long orderItemId){
        OrderItem orderItem = order.getOrderItemById(orderItemId);
        if (orderItem.getService() != null) {
            order.removeOrderItemById(orderItemId);
            repository.save(order);
        } else {
            order.removeOrderItemById(orderItemId);
            ReceivingHistory receivingHistory = receivingHistoryService.findByOrderItemId(orderItem.getId());
            receivingHistoryService.delete(receivingHistory.getId());
            Storage storage = storageService.get(orderItem.getProduct().getId(), orderItem.getServiceCenter().getId());
            storage.setQuantity(storage.getQuantity() + orderItem.getQuantity());
            storageService.save(storage);
            repository.save(order);
        }
    }

    /*public List<Order> getOrdersByDate(Date date1, Date date2, String type){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date2);
        cal.add(Calendar.DATE ,1);
        date2=(cal.getTime());
        switch (type){
            case "accepted":{
                return repository.findAllByAccepteddateBetween(date1,date2);
            }
            case "done":{
                return repository.findAllByDonedateBetween(date1,date2);
            }
            case "given":{
                return repository.findAllByGavedateBetween(date1,date2);
            }
            default:{
                return null;
            }
        }
    }*/

    public NetProfit getProfit(Long serviceCenterId) {
        return repository.getProfit(serviceCenterId);
    }

    public OrderDtoResponse setPaymentType(Long orderId, Long serviceCenterId,String type){
        Order order = get(orderId, serviceCenterId);
        TypesOfPayments typesOfPayments=TypesOfPayments.cash;
        switch (type){
            case "cash":
                typesOfPayments = TypesOfPayments.cash;
                break;
            case "online":
                typesOfPayments = TypesOfPayments.online;
                break;
            case "contract":
                typesOfPayments = TypesOfPayments.contract;
                break;
        }
        order.setTypesOfPayments(typesOfPayments);
        order.setStatus(Status.GIVEN);
        order.setGaveDate(new Date());
        return modelToDtoResponse(save(order));
    }

    public OrderDtoResponse updatePaymentType(Long orderId, Long serviceCenterId,String payment){
        Order order = get(orderId, serviceCenterId);
        switch (payment){
            case "cash":
                order.setTypesOfPayments(TypesOfPayments.cash);
                break;
            case "online":
                order.setTypesOfPayments(TypesOfPayments.online);
                break;
            case "contract":
                order.setTypesOfPayments(TypesOfPayments.contract);
                break;
        }
        return modelToDtoResponse(save(order));
    }

    public Page<OrderForListDtoResponse> pageToDtoPage(Page<Order> modelPage) {
        final Page<OrderForListDtoResponse> dtoPage = modelPage.map(this::modelToOrderForListDtoResponse);
        return dtoPage;
    }

    public OrderDtoResponse modelToDtoResponse(Order model) {
        return OrderFacade.modelToOrderDtoResponse(model);
    }

    public OrderForListDtoResponse modelToOrderForListDtoResponse(Order model) {
        return OrderFacade.modelToOrderForListDtoResponse(model);
    }

    public Order get(Long orderId, Long serviceCenterId) {
        return repository.findByIdAndServiceCenterId(orderId, serviceCenterId);
    }

    public Order getById(Long orderId)  {
        return repository.findById(orderId).get();
    }

    public boolean existsByIdAndServiceCenterId(Long orderId, Long serviceCenterId) {
        return repository.existsByIdAndServiceCenterId(orderId, serviceCenterId);
    }

    public  boolean  onlyDigits(String str, int n)
    {
        for (int i = 0; i < n; i++) {
            if (Character.isDigit(str.charAt(i))) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    public boolean existsByIdAndToken(Long id, String token) {
        return repository.existsByIdAndToken(id, token);
    }

    public Order save(Order order) {
        return repository.save(order);
    }
}
