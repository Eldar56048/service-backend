package com.crm.servicebackend.controller;

import com.crm.servicebackend.dto.requestDto.product.ProductAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.product.ProductUpdateDtoRequest;
import com.crm.servicebackend.dto.requestDto.storage.AddProductToStorageDtoRequest;
import com.crm.servicebackend.exception.domain.DtoException;
import com.crm.servicebackend.exception.domain.ResourceNotFoundException;
import com.crm.servicebackend.model.User;
import com.crm.servicebackend.service.IncomingHistoryService;
import com.crm.servicebackend.service.ProductService;
import com.crm.servicebackend.service.ProviderService;
import com.crm.servicebackend.service.ServiceCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ServiceCenterService serviceCenterService;
    private final ProductService service;
    private final IncomingHistoryService incomingHistoryService;
    private final ProviderService providerService;

    @Autowired
    public ProductController(ServiceCenterService serviceCenterService, ProductService service, IncomingHistoryService incomingHistoryService, ProviderService providerService) {
        this.serviceCenterService = serviceCenterService;
        this.service = service;
        this.incomingHistoryService = incomingHistoryService;
        this.providerService = providerService;
    }

    @GetMapping("/select")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getAllForSelect(@AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        return ResponseEntity.ok(service.getProductsForSelect(serviceCenterId));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> getAll(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String orderBy,
            @RequestParam(defaultValue = "") String title
    ) {
        Map<String, Object> response;
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (title.length()<=0)
            response = service.getAll(serviceCenterId,page-1, size, sortBy, orderBy);
        else
            response = service.getAllAndFilter(serviceCenterId,page-1, size, sortBy, orderBy, title);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> add(@AuthenticationPrincipal User user, @Valid @RequestBody ProductAddDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        return ResponseEntity.ok(service.add(serviceCenterId, dto));
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> get(@AuthenticationPrincipal User user, @PathVariable Long productId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(productId, serviceCenterId))
            throw new ResourceNotFoundException("Товар с идентификатором № "+productId+" не найдено", "product/not-found");
        return ResponseEntity.ok((service.getDtoResponse(productId, serviceCenterId)));
    }

    @GetMapping("/top-sale")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getProductTopSale(@AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        return ResponseEntity.ok(service.getTopSale(serviceCenterId));
    }

    @GetMapping("/sold-count")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getSoldProductCount(@AuthenticationPrincipal User user) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        return ResponseEntity.ok(service.getSoldProductCount(serviceCenterId));
    }

    @GetMapping("/{productId}/sold")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getProductSoldCount(@AuthenticationPrincipal User user,@PathVariable Long productId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(productId, serviceCenterId))
            throw new ResourceNotFoundException("Товар с идентификатором № "+productId+" не найдено", "product/not-found");
        return ResponseEntity.ok(service.getProductSoldCount(productId, serviceCenterId).getSoldCount());
    }

    @GetMapping("/{productId}/monthly/sold")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getMonthlySold(@AuthenticationPrincipal User user, @PathVariable long productId) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(productId, serviceCenterId))
            throw new ResourceNotFoundException("Товар с идентификатором № "+productId+" не найдено", "product/not-found");
        return ResponseEntity.ok(service.getMonthlySales(productId, serviceCenterId));
    }

    @GetMapping("/{productId}/last/purchase")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getLastPurchasePrice(@AuthenticationPrincipal User user,@PathVariable long productId){
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(productId, serviceCenterId))
            throw new ResourceNotFoundException("Товар с идентификатором № "+productId+" не найдено", "product/not-found");
        return ResponseEntity.ok(service.getLastPrice(productId, serviceCenterId));
    }

    @PostMapping("/{productId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user, @PathVariable long productId, @Valid @RequestBody ProductUpdateDtoRequest dto) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if (dto.getId()!=productId)
            throw new DtoException("Два разных id", "product/two-another-id");
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(productId, serviceCenterId))
            throw new ResourceNotFoundException("Товар с идентификатором № "+productId+" не найдено", "product/not-found");
        return ResponseEntity.ok(service.update(productId,serviceCenterId, dto));
    }

    @GetMapping("/top-profit-products")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getTopProfitProducts(@AuthenticationPrincipal User user, @RequestParam(defaultValue = "5") Long limit) {
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        return ResponseEntity.ok(service.getTopProfitProducts(serviceCenterId, limit));
    }

    @PostMapping("/{productId}/add")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> addProductToStorage(@AuthenticationPrincipal User user, @PathVariable long productId, @Valid @RequestBody AddProductToStorageDtoRequest dto){
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(productId, serviceCenterId))
            throw new ResourceNotFoundException("Товар с идентификатором № "+productId+" не найдено", "product/not-found");
        if (!providerService.existsByIdAndServiceCenterId(dto.getProviderId(), serviceCenterId))
            throw new ResourceNotFoundException("Поставщик с идентификатором № "+ dto.getProviderId()+" не найдено", "provider/not-found");
        return ResponseEntity.ok(service.addProductStorage(productId, serviceCenterId,dto));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteProduct(@AuthenticationPrincipal User user, @PathVariable long productId){
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(productId, serviceCenterId))
            throw new ResourceNotFoundException("Товар с идентификатором № "+productId+" не найдено", "product/not-found");
        service.delete(productId,serviceCenterId);
        return ResponseEntity.ok("product/deleted");
    }

    @GetMapping("/{productId}/history/incoming")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getProductHistoryIncoming(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String orderBy,
            @RequestParam(defaultValue = "") String title,
            @PathVariable long productId
    ){
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(productId, serviceCenterId))
            throw new ResourceNotFoundException("Товар с идентификатором № "+productId+" не найдено", "product/not-found");
        Map<String, Object> response;
        if (title.length()<=0)
            response = incomingHistoryService.getAllByProduct(productId, serviceCenterId,page-1, size, sortBy, orderBy);
        else
            response = incomingHistoryService.getAllByProductAndFilter(productId, serviceCenterId,page-1, size, sortBy, orderBy, title);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}/history/incoming/{historyId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteIncomingHistory(@AuthenticationPrincipal User user, @PathVariable long productId,@PathVariable  long historyId){
        Long serviceCenterId = user.getServiceCenter().getId();
        if(!serviceCenterService.existsById(serviceCenterId))
            throw new ResourceNotFoundException("Сервисный центр с идентификатором № "+serviceCenterId+" не найдено", "service-center/not-found");
        if (!service.existsByIdAndServiceCenterId(productId, serviceCenterId))
            throw new ResourceNotFoundException("Товар с идентификатором № "+productId+" не найдено", "product/not-found");
        if (!incomingHistoryService.existsByIdAndProductIdAndServiceCenterId(historyId, productId, serviceCenterId)) {
            throw new ResourceNotFoundException("История добавление товара с идентификатором № "+historyId+" не найдено", "incoming-history/not-found");
        }
        service.deleteIncomingHistory(serviceCenterId,productId,historyId);
        return ResponseEntity.status(HttpStatus.OK).body("product/incoming-history-successfully-deleted");
    }
}
