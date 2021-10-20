package com.crm.servicebackend.service;

import com.crm.servicebackend.dto.requestDto.client.ClientAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.client.ClientUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.client.ClientDtoResponse;
import com.crm.servicebackend.model.Client;
import com.crm.servicebackend.model.ServiceCenter;
import com.crm.servicebackend.repository.ClientRepository;
import com.crm.servicebackend.utils.facade.ClientFacade;
import com.crm.servicebackend.utils.facade.PaginationResponseFacade;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final DiscountService discountService;
    private final ServiceCenterService serviceCenterService;

    @Autowired
    public ClientService(ClientRepository repository, DiscountService discountService, ServiceCenterService serviceCenterService) {
        this.repository = repository;
        this.discountService = discountService;
        this.serviceCenterService = serviceCenterService;
    }

    public ClientDtoResponse add(Long serviceCenterId, ClientAddDtoRequest dto) {
        Client client = addDtoToModel(dto);
        client.setDiscount(discountService.get(dto.getDiscountId(), serviceCenterId));
        client.setServiceCenter(serviceCenterService.get(serviceCenterId));
        return modelToDtoResponse(save(client));
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

    public List<ClientDtoResponse> getAllForSelect(Long serviceCenterId) {
        return ClientFacade.modelListToDtoResponseList(repository.getAllByServiceCenterId(serviceCenterId));
    }

    public ClientDtoResponse update(Long clientId, Long serviceCenterId, ClientUpdateDtoRequest dto) {
        Client client = get(clientId, serviceCenterId);
        if (client.getDiscount().getId() != dto.getDiscountId())
            client.setDiscount(discountService.get(dto.getDiscountId(), serviceCenterId));
        client = updateDtoToModel(client, dto);
        return modelToDtoResponse(save(client));
    }

    public Client get(Long clientId, Long serviceCenterId) {
        return repository.findByIdAndServiceCenterId(clientId, serviceCenterId);
    }

    public Page<ClientDtoResponse> pageToDtoPage(Page<Client> modelPage) {
        final Page<ClientDtoResponse> dtoPage = modelPage.map(this::modelToDtoResponse);
        return dtoPage;
    }

    public ClientDtoResponse modelToDtoResponse(Client model) {
        return ClientFacade.modelToDtoResponse(model);
    }

    public Client updateDtoToModel(Client model, ClientUpdateDtoRequest dto) {
        return ClientFacade.updateDtoToModel(model, dto);
    }

    public Client addDtoToModel(ClientAddDtoRequest dto) {
        return ClientFacade.addDtoToModel(dto);
    }

    public Client save(Client client) {
        return repository.save(client);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Boolean existsByIdAndServiceCenterId(Long clientId, Long serviceCenterId) {
        return repository.existsByIdAndServiceCenterId(clientId, serviceCenterId);
    }

    public void readClientJson(ServiceCenter serviceCenter) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("clients.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray employeeList = (JSONArray) obj;

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

    public void parseJson(JSONObject clientJson, ServiceCenter serviceCenter) {
        Client client = new Client();
        client.setName((String) clientJson.get("client_name"));
        client.setSurname((String) clientJson.get("client_surname"));
        client.setPhoneNumber((String) clientJson.get("phone_number"));
        client.setDiscount(discountService.getByName((String) clientJson.get("discount_name"), serviceCenter.getId()));
        client.setServiceCenter(serviceCenter);
        repository.save(client);
    }
}
