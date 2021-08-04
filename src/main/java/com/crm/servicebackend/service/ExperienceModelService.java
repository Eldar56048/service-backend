package com.crm.servicebackend.service;

import com.crm.servicebackend.dto.requestDto.experienceModel.ExperienceModelAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.experienceModel.ExperienceModelUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.experienceModel.ExperienceModelDtoResponse;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.model.ExperienceModel;
import com.crm.servicebackend.repository.ExperienceModelRepository;
import com.crm.servicebackend.utils.facade.ExperienceModelFacade;
import com.crm.servicebackend.utils.facade.PaginationResponseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.crm.servicebackend.constant.model.experienceModel.ExperienceModelResponseCode.EXPERIENCE_MODEL_EXISTS_BY_COEFFICIENT_CODE;
import static com.crm.servicebackend.constant.model.experienceModel.ExperienceModelResponseCode.EXPERIENCE_MODEL_EXISTS_BY_NAME_CODE;
import static com.crm.servicebackend.constant.model.experienceModel.ExperienceModelResponseMessage.EXPERIENCE_MODEL_EXISTS_BY_COEFFICIENT_MESSAGE;
import static com.crm.servicebackend.constant.model.experienceModel.ExperienceModelResponseMessage.EXPERIENCE_MODEL_EXISTS_BY_NAME_MESSAGE;

@Service
public class ExperienceModelService {

    private final ExperienceModelRepository repository;
    private final ServiceCenterService serviceCenterService;

    @Autowired
    public ExperienceModelService(ExperienceModelRepository repository, DiscountService discountService, ServiceCenterService serviceCenterService) {
        this.repository = repository;
        this.serviceCenterService = serviceCenterService;
    }

    public ExperienceModelDtoResponse add(Long serviceCenterId, ExperienceModelAddDtoRequest dto) {
        if (existsByNameAndServiceCenterId(dto.getName(), serviceCenterId))
            throw new DtoException(EXPERIENCE_MODEL_EXISTS_BY_NAME_MESSAGE, EXPERIENCE_MODEL_EXISTS_BY_NAME_CODE);
        if (existsByCoefficientAndServiceCenterId(dto.getCoefficient(), serviceCenterId))
            throw new DtoException(EXPERIENCE_MODEL_EXISTS_BY_COEFFICIENT_MESSAGE, EXPERIENCE_MODEL_EXISTS_BY_COEFFICIENT_CODE);
        ExperienceModel experienceModel = addDtoToModel(dto);
        experienceModel.setServiceCenter(serviceCenterService.get(serviceCenterId));
        return modelToDtoResponse(save(experienceModel));
    }

    public List<ExperienceModelDtoResponse> getAll(Long serviceCenterId) {
        return ExperienceModelFacade.modelListToDtoResponseList(this.repository.getAllByServiceCenterId(serviceCenterId));
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

    public ExperienceModelDtoResponse update(Long experienceId, Long serviceCenterId, ExperienceModelUpdateDtoRequest dto) {
        if (existsByNameAndIdNotLikeAndServiceCenterId(dto.getName(), experienceId, serviceCenterId))
            throw new DtoException(EXPERIENCE_MODEL_EXISTS_BY_NAME_MESSAGE, EXPERIENCE_MODEL_EXISTS_BY_NAME_CODE);
        if (existsByCoefficientAndIdNotLikeAndServiceCenterId(dto.getCoefficient(), experienceId, serviceCenterId))
            throw new DtoException(EXPERIENCE_MODEL_EXISTS_BY_COEFFICIENT_MESSAGE, EXPERIENCE_MODEL_EXISTS_BY_COEFFICIENT_CODE);
        ExperienceModel experienceModel = get(experienceId, serviceCenterId);
        experienceModel = updateDtoToModel(experienceModel,dto);
        return modelToDtoResponse(save(experienceModel));
    }

    public ExperienceModel get(Long experienceId, Long serviceCenterId) {
        return repository.findByIdAndServiceCenterId(experienceId, serviceCenterId);
    }

    public Page<ExperienceModelDtoResponse> pageToDtoPage(Page<ExperienceModel> modelPage) {
        final Page<ExperienceModelDtoResponse> dtoPage = modelPage.map(this::modelToDtoResponse);
        return dtoPage;
    }

    public ExperienceModelDtoResponse modelToDtoResponse(ExperienceModel model) {
        return ExperienceModelFacade.modelToDtoResponse(model);
    }

    public ExperienceModel updateDtoToModel(ExperienceModel model, ExperienceModelUpdateDtoRequest dto) {
        return ExperienceModelFacade.updateDtoToModel(model, dto);
    }

    public ExperienceModel addDtoToModel(ExperienceModelAddDtoRequest dto) {
        return ExperienceModelFacade.addDtoToModel(dto);
    }

    public ExperienceModel save(ExperienceModel experienceModel) {
        return repository.save(experienceModel);
    }

    public Boolean existsByNameAndServiceCenterId(String name, Long serviceCenterId) {
        return repository.existsByNameAndServiceCenterId(name, serviceCenterId);
    }

    public Boolean existsByNameAndIdNotLikeAndServiceCenterId(String name, Long experienceId, Long serviceCenterId) {
        return repository.existsByNameAndIdNotLikeAndServiceCenterId(name, experienceId, serviceCenterId);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Boolean existsByIdAndServiceCenterId(Long experienceId, Long serviceCenterId) {
        return repository.existsByIdAndServiceCenterId(experienceId, serviceCenterId);
    }

    public Boolean existsByCoefficientAndServiceCenterId(int coefficient, Long serviceCenterId) {
        return repository.existsByCoefficientAndServiceCenterId(coefficient, serviceCenterId);
    }

    public Boolean existsByCoefficientAndIdNotLikeAndServiceCenterId(int coefficient, Long experienceId, Long serviceCenterId) {
        return repository.existsByCoefficientAndIdNotLikeAndServiceCenterId(coefficient, experienceId, serviceCenterId);
    }
}
