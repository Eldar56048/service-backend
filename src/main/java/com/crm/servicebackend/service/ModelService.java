package com.crm.servicebackend.service;

import com.crm.servicebackend.dto.requestDto.model.ModelAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.model.ModelUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.model.ModelDtoResponse;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.model.Model;
import com.crm.servicebackend.repository.ModelRepository;
import com.crm.servicebackend.utils.facade.ModelFacade;
import com.crm.servicebackend.utils.facade.PaginationResponseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ModelService {

    private final ModelRepository repository;
    private final ServiceCenterService serviceCenterService;

    @Autowired
    public ModelService(ModelRepository repository, ServiceCenterService serviceCenterService) {
        this.repository = repository;
        this.serviceCenterService = serviceCenterService;
    }

    public ModelDtoResponse add(Long serviceCenterId, ModelAddDtoRequest dto) {
        if (existsByNameAndServiceCenterId(dto.getName(), serviceCenterId))
            throw new DtoException("Модель с таким названием уже существует","model/exists-by-name");
        Model model = addDtoToModel(dto);
        model.setServiceCenter(serviceCenterService.get(serviceCenterId));
        return modelToDtoResponse(save(model));
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

    public List<ModelDtoResponse> getAllForSelect(Long serviceCenterId) {
        return ModelFacade.modelListToDtoResponseList(repository.getAllByServiceCenterId(serviceCenterId));
    }

    public ModelDtoResponse update(Long modelId, Long serviceCenterId, ModelUpdateDtoRequest dto) {
        if (existsByNameAndIdNotLikeAndServiceCenterId(dto.getName(), modelId, serviceCenterId))
            throw new DtoException("Модель с таким названием уже существует","model/exists-by-name");
        Model model = get(modelId, serviceCenterId);
        model = updateDtoToModel(model, dto);
        return modelToDtoResponse(save(model));
    }

    public Model get(Long modelId, Long serviceCenterId) {
        return repository.findByIdAndServiceCenterId(modelId, serviceCenterId);
    }

    public Page<ModelDtoResponse> pageToDtoPage(Page<Model> modelPage) {
        final Page<ModelDtoResponse> dtoPage = modelPage.map(this::modelToDtoResponse);
        return dtoPage;
    }

    public ModelDtoResponse modelToDtoResponse(Model model) {
        return ModelFacade.modelToDtoResponse(model);
    }

    public Model updateDtoToModel(Model model, ModelUpdateDtoRequest dto) {
        return ModelFacade.updateDtoToModel(model, dto);
    }

    public Model addDtoToModel(ModelAddDtoRequest dto) {
        return ModelFacade.addDtoToModel(dto);
    }

    public Model save(Model model) {
        return repository.save(model);
    }

    public Boolean existsByNameAndServiceCenterId(String name, Long serviceCenterId) {
        return repository.existsByNameAndServiceCenterId(name, serviceCenterId);
    }

    public Boolean existsByNameAndIdNotLikeAndServiceCenterId(String name, Long modelId, Long serviceCenterId) {
        return repository.existsByNameAndIdNotLikeAndServiceCenterId(name, modelId, serviceCenterId);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Boolean existsByIdAndServiceCenterId(Long modelId, Long serviceCenterId) {
        return repository.existsByIdAndServiceCenterId(modelId, serviceCenterId);
    }
}
