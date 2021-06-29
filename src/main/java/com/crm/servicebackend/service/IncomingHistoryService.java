package com.crm.servicebackend.service;

import com.crm.servicebackend.dto.responseDto.incomingHistory.IncomingHistoryDtoResponse;
import com.crm.servicebackend.model.IncomingHistory;
import com.crm.servicebackend.repository.IncomingHistoryRepository;
import com.crm.servicebackend.utils.facade.IncomingHistoryFacade;
import com.crm.servicebackend.utils.facade.PaginationResponseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IncomingHistoryService {
    private final IncomingHistoryRepository repository;

    @Autowired
    public IncomingHistoryService(IncomingHistoryRepository repository) {
        this.repository = repository;
    }

    public IncomingHistory save(IncomingHistory incomingHistory) {
        return repository.save(incomingHistory);
    }

    public Map<String, Object> getAllByProduct(Long productId, Long serviceCenterId, int page, int size, String sortBy, String orderBy){
        Pageable pageable;
        if (orderBy.equals("asc"))
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return PaginationResponseFacade.response(pageToDtoPage(repository.findAllByProductIdAndServiceCenterId(productId,serviceCenterId, pageable)));
    }

    public Map<String, Object> getAllByProductAndFilter(Long productId, Long serviceCenterId, int page, int size, String sortBy, String orderBy, String title){
        Pageable pageable;
        if (orderBy.equals("asc"))
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return PaginationResponseFacade.response(pageToDtoPage(repository.findAllByProductIdAndServiceCenterIdAndFilter(productId,serviceCenterId, title, pageable)));
    }

    public void deleteAllByProduct(Long productId) {
        repository.deleteAllByProductId(productId);
    }

    public IncomingHistory get(Long id, Long serviceCenterId) {
        return repository.getByIdAndServiceCenterId(id, serviceCenterId);
    }

    public int getLastPriceProduct(Long productId, Long serviceCenterId){
        IncomingHistory incomingHistory = repository.getFirstByProductIdAndServiceCenterIdOrderByDateDesc(productId, serviceCenterId);
        if(incomingHistory != null){
            return incomingHistory.getPrice();
        }
        return 0;
    }


    public Page<IncomingHistoryDtoResponse> pageToDtoPage(Page<IncomingHistory> modelPage) {
        final Page<IncomingHistoryDtoResponse> dtoPage = modelPage.map(this::modelToDtoResponse);
        return dtoPage;
    }

    public IncomingHistoryDtoResponse modelToDtoResponse(IncomingHistory model) {
        return IncomingHistoryFacade.modelToDtoResponse(model);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean existsByIdAndProductIdAndServiceCenterId(Long historyId, Long productId, Long serviceCenterId) {
        return repository.existsByIdAndProductIdAndServiceCenterId(historyId, productId, serviceCenterId);
    }
}
