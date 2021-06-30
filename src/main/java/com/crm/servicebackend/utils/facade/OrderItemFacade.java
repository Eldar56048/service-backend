package com.crm.servicebackend.utils.facade;

import com.crm.servicebackend.dto.responseDto.orderItem.OrderItemOrderDtoResponse;
import com.crm.servicebackend.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItemFacade {
    public static OrderItemOrderDtoResponse modelToOrdersOrderItemDtoResponse(OrderItem model) {
        OrderItemOrderDtoResponse dto = new OrderItemOrderDtoResponse();
        if (model.getId() != null)
            dto.setId(model.getId());
        if (model.getOrderId() != null)
            dto.setOrderId(model.getOrderId());
        if (model.getService() != null)
            dto.setService(ServiceFacade.modelToItemDto(model.getService()));
        if (model.getProduct() != null)
            dto.setProduct(ProductFacade.modelToItemDtoResponse(model.getProduct()));
        if (model.getDoneUser() != null)
            dto.setDoneUser(UserFacade.modelToItemDto(model.getDoneUser()));
        dto.setSoldPrice(model.getSoldPrice());
        dto.setQuantity(model.getQuantity());
        return dto;
    }

    public static List<OrderItemOrderDtoResponse> listOfModelToListOfOrdersOrderItemDtoResponse(List<OrderItem> modelList) {
        List<OrderItemOrderDtoResponse> dtoList = new ArrayList<>();
        for(OrderItem model:modelList) {
            dtoList.add(modelToOrdersOrderItemDtoResponse(model));
        }
        return dtoList;
    }
}
