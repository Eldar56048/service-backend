package com.crm.servicebackend.service;

import com.crm.servicebackend.dto.requestDto.product.ProductAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.product.ProductUpdateDtoRequest;
import com.crm.servicebackend.dto.requestDto.storage.AddProductToStorageDtoRequest;
import com.crm.servicebackend.dto.responseDto.incomingHistory.IncomingHistoryDtoResponse;
import com.crm.servicebackend.dto.responseDto.product.ProductDtoResponse;
import com.crm.servicebackend.dto.responseDto.product.ProductForSelectDtoResponse;
import com.crm.servicebackend.dto.responseDto.statistics.*;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.model.*;
import com.crm.servicebackend.repository.ProductRepository;
import com.crm.servicebackend.utils.facade.PaginationResponseFacade;
import com.crm.servicebackend.utils.facade.ProductFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class ProductService {
    private final ProductRepository repository;
    private final ServiceCenterService serviceCenterService;
    private final StorageService storageService;
    private final ProviderService providerService;
    private final IncomingHistoryService incomingHistoryService;
    private final ReceivingHistoryService receivingHistoryService;
    private final OrderItemService orderItemService;
    private final OrderService orderService;

    @Autowired
    public ProductService(ProductRepository repository, ServiceCenterService serviceCenterService, StorageService storageService, ProviderService providerService, IncomingHistoryService incomingHistoryService, ReceivingHistoryService receivingHistoryService, OrderItemService orderItemService, OrderService orderService) {
        this.repository = repository;
        this.serviceCenterService = serviceCenterService;
        this.storageService = storageService;
        this.providerService = providerService;
        this.incomingHistoryService = incomingHistoryService;
        this.receivingHistoryService = receivingHistoryService;
        this.orderItemService = orderItemService;
        this.orderService = orderService;
    }

    public ProductDtoResponse add(Long serviceCenterId, ProductAddDtoRequest dto) {
        if (existsByNameAndServiceCenterId(dto.getName(), serviceCenterId))
            throw new DtoException("Товар с таким наименованием уже существует","product/exists-by-name");
        ServiceCenter serviceCenter = serviceCenterService.get(serviceCenterId);
        Product product = addDtoToModel(dto);
        product.setServiceCenter(serviceCenter);
        product = save(product);
        Storage storage = new Storage(serviceCenter,product, 0);
        storage = storageService.save(storage);
        return modelToDtoResponse(storage);
    }

    public List<ProductForSelectDtoResponse> getProductsForSelect(Long serviceCenterId) {
        return repository.getProductsForSelect(serviceCenterId);
    }

    public Map<String, Object> getAll(Long serviceCenterId, int page, int size, String sortBy, String orderBy){
        Pageable pageable;
        if (orderBy.equals("asc"))
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return PaginationResponseFacade.response((repository.findAll(serviceCenterId, pageable)));
    }

    public Map<String, Object> getAllAndFilter(Long serviceCenterId, int page, int size, String sortBy, String orderBy, String title){
        Pageable pageable;
        if (orderBy.equals("asc"))
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return PaginationResponseFacade.response((repository.findAllAndFilter(serviceCenterId, title, pageable)));
    }

    public ProductDtoResponse update(Long productId, Long serviceCenterId, ProductUpdateDtoRequest dto) {
        if (existsByNameAndIdNotLikeAndServiceCenterId(dto.getName(), productId, serviceCenterId))
            throw new DtoException("Товар с таким наименованием уже существует","product/exists-by-name");
        Product product = get(productId, serviceCenterId);
        product = updateDtoToModel(product, dto);
        product = save(product);
        return getDtoResponse(productId, serviceCenterId);
    }

    public TopSaleItem getTopSale(Long serviceCenterId) {
        return repository.getTopSaleProduct(serviceCenterId);
    }



    public IncomingHistoryDtoResponse addProductStorage(Long productId, Long serviceCenterId, AddProductToStorageDtoRequest dto){
        Product product = get(productId, serviceCenterId);
        Provider provider = providerService.get(dto.getProviderId(), serviceCenterId);
        IncomingHistory incomingHistory = new IncomingHistory(product, provider, dto.getQuantity(), dto.getPrice(), serviceCenterService.get(serviceCenterId));
        incomingHistory = incomingHistoryService.save(incomingHistory);
        storageService.updateStorageCount(product.getId(), serviceCenterId,dto.getQuantity());
        return incomingHistoryService.modelToDtoResponse(incomingHistory);
    }

    public SoldItemCount getSoldProductCount(Long serviceCenterId) {
        return repository.getSoldProductCount(serviceCenterId);
    }



    public List<MonthlySale> getMonthlySales(long productId, long serviceCenterId){
        return repository.getMonthlySold(productId, serviceCenterId);
    }

    public List<TopProfitItem> getTopProfitProducts(Long serviceCenterId, long limit) {
        return repository.getTopProfitProducts(serviceCenterId, limit);
    }

    public Sold getProductSoldCount(Long productId, Long serviceCenterId) {
        return repository.getProductSoldCount(productId, serviceCenterId);
    }

    public int getLastPrice(long id, Long serviceCenterId){
        return incomingHistoryService.getLastPriceProduct(id, serviceCenterId);
    }


    public Product get(Long productId, Long serviceCenterId) {
        return repository.findByIdAndServiceCenterId(productId, serviceCenterId);
    }

    public ProductDtoResponse getDtoResponse(Long productId, Long serviceCenterId) {
        return modelToDtoResponse(storageService.get(productId, serviceCenterId));
    }


    public ProductDtoResponse modelToDtoResponse(Storage model) {
        return ProductFacade.modelToDtoResponse(model);
    }

    public Product updateDtoToModel(Product model, ProductUpdateDtoRequest dto) {
        return ProductFacade.updateDtoToModel(model, dto);
    }

    public Product addDtoToModel(ProductAddDtoRequest dto) {
        return ProductFacade.addDtoToModel(dto);
    }

    public  Product save(Product product) {
        return repository.save(product);
    }

    public Boolean existsByNameAndServiceCenterId(String name, Long serviceCenterId) {
        return repository.existsByNameAndServiceCenterId(name, serviceCenterId);
    }

    public Boolean existsByNameAndIdNotLikeAndServiceCenterId(String name, Long productId, Long serviceCenterId) {
        return repository.existsByNameAndIdNotLikeAndServiceCenterId(name, productId, serviceCenterId);
    }

    public void delete(Long productId, Long serviceCenterId) {
        Product product = get(productId, serviceCenterId);
        Storage storage = storageService.get(productId, serviceCenterId);
        incomingHistoryService.deleteAllByProduct(productId);
        receivingHistoryService.deleteAllByOrderItemProductAndServiceCenterId(product, serviceCenterId);
        List<OrderItem> orderItems = orderItemService.getAllByProductAndServiceCenterId(product, serviceCenterId);
        for(OrderItem orderItem:orderItems){
            Order order = orderService.get(orderItem.getOrderId(), serviceCenterId);
            order.getItems().remove(orderItem);
            orderService.save(order);
        }
        storageService.delete(storage);
        repository.delete(product);
    }

    public Boolean existsByIdAndServiceCenterId(Long productId, Long serviceCenterId) {
        return repository.existsByIdAndServiceCenterId(productId, serviceCenterId);
    }

    public void deleteIncomingHistory(Long serviceCenterId, long productId,long historyId){
        IncomingHistory incomingHistory = incomingHistoryService.get(historyId, serviceCenterId);
        Storage storage = storageService.get(productId, serviceCenterId);
        if(storage.getQuantity()>=incomingHistory.getQuantity()){
            storage.setQuantity(storage.getQuantity()-incomingHistory.getQuantity());
            incomingHistoryService.deleteById(historyId);
            storageService.save(storage);
        } else {
            throw new DtoException("Невозможно удалить историю добавление №"+historyId+" так как на складе недостаточно товаров. Нужно еще "+(incomingHistory.getQuantity()-storage.getQuantity()), "incoming-history/not-enough-in-storage");
        }
    }

}
