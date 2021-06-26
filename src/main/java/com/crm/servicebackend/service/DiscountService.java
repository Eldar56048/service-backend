package com.crm.servicebackend.service;

import com.crm.servicebackend.dto.requestDto.discount.DiscountAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.discount.DiscountUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.discount.DiscountDtoResponse;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.model.Discount;
import com.crm.servicebackend.repository.DiscountRepository;
import com.crm.servicebackend.utils.facade.DiscountFacade;
import com.crm.servicebackend.utils.facade.PaginationResponseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DiscountService {

    private final DiscountRepository repository;
    private final ServiceCenterService serviceCenterService;

    @Autowired
    public DiscountService(DiscountRepository repository, ServiceCenterService serviceCenterService) {
        this.repository = repository;
        this.serviceCenterService = serviceCenterService;
    }

    public DiscountDtoResponse add(Long serviceCenterId, DiscountAddDtoRequest dto) {
        if (existsByDiscountNameAndServiceCenterId(dto.getDiscountName(), serviceCenterId))
            throw new DtoException("Скидка с таким названием уже существует","discount/exists-by-name");
        Discount discount = addDtoToModel(dto);
        discount.setServiceCenter(serviceCenterService.get(serviceCenterId));
        return modelToDtoResponse(save(discount));
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

    public DiscountDtoResponse update(Long discountId, Long serviceCenterId, DiscountUpdateDtoRequest dto) {
        if (existsByDiscountNameAndIdNotLikeAndServiceCenterId(dto.getDiscountName(), discountId, serviceCenterId))
            throw new DtoException("Скидка с таким названием уже существует","discount/exists-by-name");
        Discount discount = get(discountId, serviceCenterId);
        discount = updateDtoToModel(discount, dto);
        return modelToDtoResponse(save(discount));
    }

    public Discount get(Long discountId, Long serviceCenterId) {
        return repository.findByIdAndServiceCenterId(discountId, serviceCenterId);
    }

    public Page<DiscountDtoResponse> pageToDtoPage(Page<Discount> modelPage) {
        final Page<DiscountDtoResponse> dtoPage = modelPage.map(this::modelToDtoResponse);
        return dtoPage;
    }

    public DiscountDtoResponse modelToDtoResponse(Discount model) {
        return DiscountFacade.modelToDtoResponse(model);
    }

    public Discount updateDtoToModel(Discount model, DiscountUpdateDtoRequest dto) {
        return DiscountFacade.updateDtoToModel(model, dto);
    }

    public Discount addDtoToModel(DiscountAddDtoRequest dto) {
        return DiscountFacade.addDtoToModel(dto);
    }

    public Discount save(Discount discount) {
        return repository.save(discount);
    }

    public Boolean existsByDiscountNameAndServiceCenterId(String name, Long serviceCenterId) {
        return repository.existsByDiscountNameAndServiceCenterId(name, serviceCenterId);
    }

    public Boolean existsByDiscountNameAndIdNotLikeAndServiceCenterId(String name, Long discountId, Long serviceCenterId) {
        return repository.existsByDiscountNameAndIdNotLikeAndServiceCenterId(name, discountId, serviceCenterId);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Boolean existsByIdAndServiceCenterId(Long discountId, Long serviceCenterId) {
        return repository.existsByIdAndServiceCenterId(discountId, serviceCenterId);
    }
}
