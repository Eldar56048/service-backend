package com.crm.servicebackend.service;

import com.crm.servicebackend.dto.requestDto.service.ServiceAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.service.ServiceUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.service.ServiceDtoResponse;
import com.crm.servicebackend.dto.responseDto.service.ServiceForSelectDtoResponse;
import com.crm.servicebackend.dto.responseDto.statistics.*;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.model.*;
import com.crm.servicebackend.repository.ServiceRepository;
import com.crm.servicebackend.utils.facade.PaginationResponseFacade;
import com.crm.servicebackend.utils.facade.ServiceFacade;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.crm.servicebackend.constant.model.service.ServiceResponseCode.SERVICE_EXISTS_BY_NAME_CODE;
import static com.crm.servicebackend.constant.model.service.ServiceResponseMessage.SERVICE_EXISTS_BY_NAME_MESSAGE;

@org.springframework.stereotype.Service
public class ServiceModelService {
    private final ServiceRepository repository;
    private final ServiceCenterService serviceCenterService;
    private final OrderItemService orderItemService;
    private final OrderService orderService;

    @Autowired
    public ServiceModelService(ServiceRepository repository, ServiceCenterService serviceCenterService, OrderItemService orderItemService, OrderService orderService) {
        this.repository = repository;
        this.serviceCenterService = serviceCenterService;
        this.orderItemService = orderItemService;
        this.orderService = orderService;
    }

    public ServiceDtoResponse add(Long serviceCenterId, ServiceAddDtoRequest dto, User user) {
        if (existsByNameAndServiceCenterId(dto.getName(), serviceCenterId))
            throw new DtoException(SERVICE_EXISTS_BY_NAME_MESSAGE, SERVICE_EXISTS_BY_NAME_CODE);
        Service service = addDtoToModel(dto, user);
        service.setServiceCenter(serviceCenterService.get(serviceCenterId));
        return modelToDtoResponse(save(service));
    }

    public List<ServiceForSelectDtoResponse> getServicesForSelect(Long serviceCenterId) {
        return repository.getServicesForSelect(serviceCenterId);
    }

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
        return PaginationResponseFacade.response(pageToDtoPage(repository.findAndFilter(serviceCenterId, title, pageable)));
    }

    public ServiceDtoResponse update(Long serviceId, Long serviceCenterId, ServiceUpdateDtoRequest dto, User user) {
        if (existsByNameAndIdNotLikeAndServiceCenterId(dto.getName(), serviceId, serviceCenterId))
            throw new DtoException(SERVICE_EXISTS_BY_NAME_MESSAGE, SERVICE_EXISTS_BY_NAME_CODE);
        Service service = get(serviceId, serviceCenterId);
        service = updateDtoToModel(service, dto, user);
        return modelToDtoResponse(save(service));
    }

    public Service get(Long serviceId, Long serviceCenterId) {
        return repository.findByIdAndServiceCenterId(serviceId, serviceCenterId);
    }

    public Sold getServiceSoldCount(Long serviceCenterId, Long serviceId){
        return repository.getServiceSoldCount(serviceId, serviceCenterId);
    }

    public List<MonthlySale> getMonthlySold(Long serviceCenterId, Long serviceId) {
        return repository.getMonthlySold(serviceId, serviceCenterId);
    }

    public List<TopProfitItem> getTopProfitServices(Long serviceCenterId, int limit) {
        return repository.getTopProfitServices(serviceCenterId, limit);
    }

    public Page<ServiceDtoResponse> pageToDtoPage(Page<Service> modelPage) {
        final Page<ServiceDtoResponse> dtoPage = modelPage.map(this::modelToDtoResponse);
        return dtoPage;
    }

    public TopSaleItem getTopSaleService(Long serviceCenterId) {
        return repository.getTopSaleService(serviceCenterId);
    }

    public SoldItemCount getSoldServiceCount(Long serviceCenterId) {
        return repository.getSoldServiceCount(serviceCenterId);
    }

    public ServiceDtoResponse modelToDtoResponse(Service model) {
        return ServiceFacade.modelToDtoResponse(model);
    }

    public Service updateDtoToModel(Service model, ServiceUpdateDtoRequest dto, User user) {
        return ServiceFacade.updateDtoToModel(model, dto, user);
    }

    public Service addDtoToModel(ServiceAddDtoRequest dto, User user) {
        return ServiceFacade.addDtoToModel(dto, user);
    }

    public Service save(Service service) {
        return repository.save(service);
    }

    public Boolean existsByNameAndServiceCenterId(String name, Long serviceCenterId) {
        return repository.existsByNameAndServiceCenterId(name, serviceCenterId);
    }

    public Boolean existsByNameAndIdNotLikeAndServiceCenterId(String name, Long serviceId, Long serviceCenterId) {
        return repository.existsByNameAndIdNotLikeAndServiceCenterId(name, serviceId, serviceCenterId);
    }

    public void delete(Long id, Long serviceCenterId) {
        Service service = get(id, serviceCenterId);
        List<OrderItem> orderItems = orderItemService.getAllByServiceAndServiceCenterId(service, serviceCenterId);
        for(OrderItem orderItem:orderItems){
            Order order = orderService.get(orderItem.getOrderId(), serviceCenterId);
            order.getItems().remove(orderItem);
            orderService.save(order);
        }
        repository.deleteById(id);
    }

    public Boolean existsByIdAndServiceCenterId(Long serviceId, Long serviceCenterId) {
        return repository.existsByIdAndServiceCenterId(serviceId, serviceCenterId);
    }

    public void readServiceJson(ServiceCenter serviceCenter) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("services.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray employeeList = (JSONArray) obj;
            System.out.println(employeeList);

            //Iterate over employee array
            employeeList.forEach( emp -> parseJson( (JSONObject) emp , serviceCenter) );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void parseJson(JSONObject serviceJson, ServiceCenter serviceCenter) {
        Service service = new Service();
        service.setName((String) serviceJson.get("service_name"));
        service.setDescription((String) serviceJson.get("service_description"));
        service.setPercentage(Integer.parseInt((String) serviceJson.get("percentage")));
        service.setPrice(Integer.parseInt((String) serviceJson.get("price")));
        service.setServiceCenter(serviceCenter);
        repository.save(service);
    }
}
