package com.crm.servicebackend.service;

import com.crm.servicebackend.dto.requestDto.type.TypeAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.type.TypeUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.type.TypeDtoResponse;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.model.Type;
import com.crm.servicebackend.repository.TypeRepository;
import com.crm.servicebackend.utils.facade.PaginationResponseFacade;
import com.crm.servicebackend.utils.facade.TypeFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TypeService {
    private final TypeRepository repository;
    private ServiceCenterService serviceCenterService;

    @Autowired
    public TypeService(TypeRepository repository, ServiceCenterService serviceCenterService) {
        this.repository = repository;
        this.serviceCenterService = serviceCenterService;
    }

    public TypeDtoResponse add(Long serviceCenterId, TypeAddDtoRequest dto) {
        if (existsByNameAndServiceCenterId(dto.getName(), serviceCenterId))
            throw new DtoException("Тип устройства с таким названием уже существует","type/exists-by-name");
        Type type = addDtoToModel(dto);
        type.setServiceCenter(serviceCenterService.get(serviceCenterId));
        return modelToDtoResponse(save(type));
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

    public List<TypeDtoResponse> getAllForSelect(Long serviceCenterId) {
        return TypeFacade.modelListToDtoResponseList(repository.getAllByServiceCenterId(serviceCenterId));
    }

    public TypeDtoResponse update(Long typeId, Long serviceCenterId, TypeUpdateDtoRequest dto) {
        if (existsByNameAndIdNotLikeAndServiceCenterId(dto.getName(), typeId, serviceCenterId))
            throw new DtoException("Тип устройства с таким названием уже существует","type/exists-by-name");
        Type type = get(typeId, serviceCenterId);
        type = updateDtoToModel(type, dto);
        return modelToDtoResponse(save(type));
    }

    public Type get(Long typeId, Long serviceCenterId) {
        return repository.findByIdAndServiceCenterId(typeId, serviceCenterId);
    }

    public Page<TypeDtoResponse> pageToDtoPage(Page<Type> modelPage) {
        final Page<TypeDtoResponse> dtoPage = modelPage.map(this::modelToDtoResponse);
        return dtoPage;
    }

    public TypeDtoResponse modelToDtoResponse(Type model) {
        return TypeFacade.modelToDtoResponse(model);
    }

    public Type updateDtoToModel(Type model, TypeUpdateDtoRequest dto) {
        return TypeFacade.updateDtoToModel(model, dto);
    }

    public Type addDtoToModel(TypeAddDtoRequest dto) {
        return TypeFacade.addDtoToModel(dto);
    }

    public Type save(Type type) {
        return repository.save(type);
    }

    public Boolean existsByNameAndServiceCenterId(String name, Long serviceCenterId) {
        return repository.existsByNameAndServiceCenterId(name, serviceCenterId);
    }

    public Boolean existsByNameAndIdNotLikeAndServiceCenterId(String name, Long typeId, Long serviceCenterId) {
        return repository.existsByNameAndIdNotLikeAndServiceCenterId(name, typeId, serviceCenterId);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Boolean existsByIdAndServiceCenterId(Long typeId, Long serviceCenterId) {
        return repository.existsByIdAndServiceCenterId(typeId, serviceCenterId);
    }
}
