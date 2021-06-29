package com.crm.servicebackend.utils.facade;

import com.crm.servicebackend.dto.requestDto.product.ProductAddDtoRequest;
import com.crm.servicebackend.dto.requestDto.product.ProductUpdateDtoRequest;
import com.crm.servicebackend.dto.responseDto.product.ProductDtoResponse;
import com.crm.servicebackend.model.Product;
import com.crm.servicebackend.model.Storage;

public class ProductFacade {
    public static Product addDtoToModel(ProductAddDtoRequest dto) {
        Product model = new Product();
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        model.setPrice(dto.getPrice());
        return model;
    }

    public static Product updateDtoToModel(Product model, ProductUpdateDtoRequest dto) {
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        model.setPrice(dto.getPrice());
        return model;
    }

    public static ProductDtoResponse modelToDtoResponse(Storage model) {
        ProductDtoResponse dto = new ProductDtoResponse();
        if (model.getProduct().getId() != null)
            dto.setId(model.getProduct().getId());
        if (model.getProduct().getName() != null)
            dto.setName(model.getProduct().getName());
        if (model.getProduct().getDescription() != null)
            dto.setDescription(model.getProduct().getDescription());
        dto.setPrice(model.getProduct().getPrice());
        dto.setStorage(model.getQuantity());
        return dto;
    }


}
