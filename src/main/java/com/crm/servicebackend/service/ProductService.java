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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.crm.servicebackend.constant.model.product.ProductResponseCode.PRODUCT_EXISTS_BY_NAME_CODE;
import static com.crm.servicebackend.constant.model.product.ProductResponseMessage.PRODUCT_EXISTS_BY_NAME_MESSAGE;
import static com.crm.servicebackend.constant.model.productIncomingHistory.ProductIncomingHistoryResponseCode.PRODUCT_NOT_ENOUGH_IN_STORAGE_CODE;
import static com.crm.servicebackend.constant.model.productIncomingHistory.ProductIncomingHistoryResponseMessage.PRODUCT_NOT_ENOUGH_IN_STORAGE_MESSAGE;

@org.springframework.stereotype.Service
public class ProductService {
    @Autowired
    private  ProductRepository repository;
    @Autowired
    private  ServiceCenterService serviceCenterService;
    @Autowired
    private  StorageService storageService;
    @Autowired
    private  ProviderService providerService;
    @Autowired
    private  IncomingHistoryService incomingHistoryService;
    @Autowired
    private  ReceivingHistoryService receivingHistoryService;
    @Autowired
    private  OrderItemService orderItemService;
    @Autowired
    private  OrderService orderService;

    public ProductDtoResponse add(Long serviceCenterId, ProductAddDtoRequest dto) {
        if (existsByNameAndServiceCenterId(dto.getName(), serviceCenterId))
            throw new DtoException(PRODUCT_EXISTS_BY_NAME_MESSAGE, PRODUCT_EXISTS_BY_NAME_CODE);
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
            throw new DtoException(PRODUCT_EXISTS_BY_NAME_MESSAGE, PRODUCT_EXISTS_BY_NAME_CODE);
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
            throw new DtoException(PRODUCT_NOT_ENOUGH_IN_STORAGE_MESSAGE(historyId, incomingHistory.getQuantity()-storage.getQuantity()), PRODUCT_NOT_ENOUGH_IN_STORAGE_CODE);
        }
    }

    public void readProductJson(ServiceCenter serviceCenter) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("products.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray employeeList = (JSONArray) obj;
            System.out.println(employeeList);

            //Iterate over employee array
            employeeList.forEach( emp -> {
                try {
                    parseJson( (JSONObject) emp , serviceCenter);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void parseJson(JSONObject productJson, ServiceCenter serviceCenter) throws java.text.ParseException {
        Product product = new Product();
        product.setName((String) productJson.get("product_name"));
        product.setDescription((String) productJson.get("description"));
        product.setPrice(Integer.parseInt((String) productJson.get("price")));
        product.setServiceCenter(serviceCenter);
        product = save(product);
        Storage storage = new Storage();
        storage.setProduct(product);
        storage.setServiceCenter(serviceCenter);
        storage.setQuantity(Integer.parseInt((String) productJson.get("storage_quantity")));
        storageService.save(storage);
        String providerName = (String) productJson.get("provider");
        int quantity = Integer.parseInt((String) productJson.get("storage_quantity"));
        if(providerName!=null&&quantity>0) {
            IncomingHistory incomingHistory = new IncomingHistory();
            incomingHistory.setProduct(product);
            incomingHistory.setPrice(Integer.parseInt((String) productJson.get("income_price")));
            incomingHistory.setServiceCenter(serviceCenter);
            incomingHistory.setQuantity(quantity);
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").parse((String) productJson.get("date"));
            incomingHistory.setDate(date);
            Provider provider = providerService.getProviderByName(providerName, serviceCenter.getId());
            incomingHistory.setProvider(provider);
            incomingHistoryService.save(incomingHistory);
        }
    }

}
