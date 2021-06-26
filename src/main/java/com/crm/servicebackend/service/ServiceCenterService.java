package com.crm.servicebackend.service;

import com.crm.servicebackend.dto.requestDto.serviceCenter.ServiceCenterAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.serviceCenter.ServiceCenterUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.serviceCenter.ServiceCenterDtoResponse;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.model.ServiceCenter;
import com.crm.servicebackend.repository.ServiceCenterRepository;
import com.crm.servicebackend.utils.facade.PaginationResponseFacade;
import com.crm.servicebackend.utils.facade.ServiceCenterFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ServiceCenterService {

    private final ServiceCenterRepository repository;

    @Autowired
    public ServiceCenterService(ServiceCenterRepository repository) {
        this.repository = repository;
    }

    public ServiceCenterDtoResponse add(ServiceCenterAddDtoRequest dto) {
        if (existsByName(dto.getName()))
            throw new DtoException("Сервисный центр с таким названием уже существует","service-center/exists-by-name");
        ServiceCenter serviceCenter = ServiceCenterFacade.addDtoToModel(dto);
        return modelToDtoResponse(save(serviceCenter));
    }

    public Map<String, Object> getAll(int page, int size, String sortBy, String orderBy){
        Pageable pageable;
        if (orderBy.equals("asc"))
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return PaginationResponseFacade.response(pageToDtoPage(repository.findAll(pageable)));
    }

    public Map<String, Object> getAllAndFilter(int page, int size, String sortBy, String orderBy, String title){
        Pageable pageable;
        if (orderBy.equals("asc"))
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return PaginationResponseFacade.response(pageToDtoPage(repository.findAndFilter(title, pageable)));
    }

    public ServiceCenterDtoResponse update(Long id, ServiceCenterUpdateDtoRequest dto) {
        ServiceCenter serviceCenter = get(id);
        if (existsByNameAndIdNotLike(dto.getName(), id))
            throw new DtoException("Сервисный центр с таким названием уже существует","service-center/exists-by-name");
        serviceCenter = ServiceCenterFacade.updateDtoToModel(serviceCenter, dto);
        return modelToDtoResponse(save(serviceCenter));
    }

    public ServiceCenter get(Long id) {
        return repository.findById(id).get();
    }

    public Page<ServiceCenterDtoResponse> pageToDtoPage(Page<ServiceCenter> modelPage) {
        final Page<ServiceCenterDtoResponse> dtoPage = modelPage.map(this::modelToDtoResponse);
        return dtoPage;
    }

    public ServiceCenterDtoResponse modelToDtoResponse(ServiceCenter serviceCenter) {
        return ServiceCenterFacade.modelToDtoResponse(serviceCenter);
    }

    public ServiceCenter updateDtoToModel(ServiceCenter model, ServiceCenterUpdateDtoRequest dto) {
        return ServiceCenterFacade.updateDtoToModel(model, dto);
    }

    public ServiceCenter save(ServiceCenter serviceCenter) {
        return repository.save(serviceCenter);
    }

    public Boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    public Boolean existsByNameAndIdNotLike(String name, Long id) {
        return repository.existsByNameAndIdNotLike(name, id);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
