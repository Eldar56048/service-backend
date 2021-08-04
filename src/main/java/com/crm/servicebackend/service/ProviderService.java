package com.crm.servicebackend.service;

import com.crm.servicebackend.dto.requestDto.providers.ProviderAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.providers.ProviderUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.provider.ProviderDtoResponse;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.model.Provider;
import com.crm.servicebackend.repository.ProviderRepository;
import com.crm.servicebackend.utils.facade.PaginationResponseFacade;
import com.crm.servicebackend.utils.facade.ProviderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.crm.servicebackend.constant.response.provider.ProviderResponseCode.PROVIDER_EXISTS_BY_NAME_CODE;
import static com.crm.servicebackend.constant.response.provider.ProviderResponseMessage.PROVIDER_EXISTS_BY_NAME_MESSAGE;


@Service
public class ProviderService {
    private final ProviderRepository repository;
    private final ServiceCenterService serviceCenterService;

    @Autowired
    public ProviderService(ProviderRepository repository, ServiceCenterService serviceCenterService) {
        this.repository = repository;
        this.serviceCenterService = serviceCenterService;
    }

    public ProviderDtoResponse add(Long serviceCenterId, ProviderAddDtoRequest dto) {
        if (existsByNameAndServiceCenterId(dto.getName(), serviceCenterId))
            throw new DtoException(PROVIDER_EXISTS_BY_NAME_MESSAGE, PROVIDER_EXISTS_BY_NAME_CODE);
        Provider provider = addDtoToModel(dto);
        provider.setServiceCenter(serviceCenterService.get(serviceCenterId));
        return modelToDtoResponse(save(provider));
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

    public ProviderDtoResponse update(Long providerId, Long serviceCenterId, ProviderUpdateDtoRequest dto) {
        if (existsByNameAndIdNotLikeAndServiceCenterId(dto.getName(), providerId, serviceCenterId))
            throw new DtoException(PROVIDER_EXISTS_BY_NAME_MESSAGE, PROVIDER_EXISTS_BY_NAME_CODE);
        Provider provider = get(providerId, serviceCenterId);
        provider = updateDtoToModel(provider, dto);
        return modelToDtoResponse(save(provider));
    }

    public List<ProviderDtoResponse> getAllForSelect(Long serviceCenterId) {
        return modelListToDtoResponseList(repository.getAllByServiceCenterId(serviceCenterId));
    }

    public Provider get(Long providerId, Long serviceCenterId) {
        return repository.findByIdAndServiceCenterId(providerId, serviceCenterId);
    }

    public Page<ProviderDtoResponse> pageToDtoPage(Page<Provider> modelPage) {
        final Page<ProviderDtoResponse> dtoPage = modelPage.map(this::modelToDtoResponse);
        return dtoPage;
    }

    public ProviderDtoResponse modelToDtoResponse(Provider model) {
        return ProviderFacade.modelToDtoResponse(model);
    }

    public Provider updateDtoToModel(Provider model, ProviderUpdateDtoRequest dto) {
        return ProviderFacade.updateDtoToModel(model, dto);
    }

    public List<ProviderDtoResponse> modelListToDtoResponseList(List<Provider> modelList) {
        return ProviderFacade.modelListToDtoResponseList(modelList);
    }

    public Provider addDtoToModel(ProviderAddDtoRequest dto) {
        return ProviderFacade.addDtoToModel(dto);
    }

    public Provider save(Provider provider) {
        return repository.save(provider);
    }

    public Boolean existsByNameAndServiceCenterId(String name, Long serviceCenterId) {
        return repository.existsByNameAndServiceCenterId(name, serviceCenterId);
    }

    public Boolean existsByNameAndIdNotLikeAndServiceCenterId(String name, Long providerId, Long serviceCenterId) {
        return repository.existsByNameAndIdNotLikeAndServiceCenterId(name, providerId, serviceCenterId);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Boolean existsByIdAndServiceCenterId(Long providerId, Long serviceCenterId) {
        return repository.existsByIdAndServiceCenterId(providerId, serviceCenterId);
    }
}
