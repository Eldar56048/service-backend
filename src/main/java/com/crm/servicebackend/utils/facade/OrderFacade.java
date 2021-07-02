package com.crm.servicebackend.utils.facade;

import com.crm.servicebackend.dto.responseDto.order.OrderDtoResponse;
import com.crm.servicebackend.dto.responseDto.order.OrderForListDtoResponse;
import com.crm.servicebackend.model.Order;

public class OrderFacade {
    public static OrderDtoResponse modelToOrderDtoResponse(Order model) {
        OrderDtoResponse dto = new OrderDtoResponse();
        if (model.getId() != null)
            dto.setId(model.getId());
        if (model.getClientName() != null)
            dto.setClientName(model.getClientName());
        if (model.getPhoneNumber() != null)
            dto.setPhoneNumber(model.getPhoneNumber());
        if (model.getProblem() != null)
            dto.setProblem(model.getProblem());
        if (model.getItems() != null)
            dto.setItems(OrderItemFacade.listOfModelToListOfOrdersOrderItemDtoResponse(model.getItems()));
        if (model.getType() != null)
            dto.setType(TypeFacade.modelToDtoResponse(model.getType()));
        if (model.getModel() != null)
            dto.setModel(ModelFacade.modelToDtoResponse(model.getModel()));
        if (model.getAcceptedUser() != null)
            dto.setAcceptedUser(UserFacade.modelToItemDto(model.getAcceptedUser()));
        if (model.getDoneUser() != null)
            dto.setDoneUser(UserFacade.modelToItemDto(model.getDoneUser()));
        if (model.getGiveUser() != null)
            dto.setGiveUser(UserFacade.modelToItemDto(model.getGiveUser()));
        if (model.getAcceptedDate() != null)
            dto.setAcceptedDate(model.getAcceptedDate());
        if (model.getGaveDate() != null)
            dto.setGaveDate(model.getGaveDate());
        if (model.getDoneDate() != null)
            dto.setDoneDate(model.getDoneDate());
        dto.setPrice(model.getPrice());
        if (model.getStatus() != null)
            dto.setStatus(model.getStatus());
        if (model.getTypesOfPayments() != null)
            dto.setTypesOfPayments(model.getTypesOfPayments());
        dto.setNotified(model.getNotified());
        if (model.getComment() != null)
            dto.setComment(model.getComment());
        if (model.getModelCompany() != null)
            dto.setModelCompany(model.getModelCompany());
        if (model.getClient() != null)
            dto.setClient(ClientFacade.modelToDtoResponse(model.getClient()));
        if (model.getDiscount() != null)
            dto.setDiscount(DiscountFacade.modelToDtoResponse(model.getDiscount()));
        if (model.getDiscountName() != null)
            dto.setDiscountName(model.getDiscountName());
        if (model.getToken() != null)
            dto.setToken(model.getToken());
        if (model.getGuaranteeDate() != null)
            dto.setGuaranteeDate(model.getGuaranteeDate());
        dto.setDiscountPercent(model.getDiscountPercent());
        return dto;
    }

    public static OrderForListDtoResponse modelToOrderForListDtoResponse(Order model) {
        OrderForListDtoResponse dto = new OrderForListDtoResponse();
        if (model.getId() != null)
            dto.setId(model.getId());
        if (model.getClientName() != null)
            dto.setClientName(model.getClientName());
        if (model.getStatus() != null)
            dto.setStatus(model.getStatus());
        if (model.getModel() != null)
            dto.setModel(model.getModel().getName());
        if (model.getType() != null)
            dto.setType(model.getType().getName());
        if (model.getAcceptedDate() != null)
            dto.setAccepted(model.getAcceptedDate());
        if (model.getPhoneNumber() != null)
            dto.setPhoneNumber(model.getPhoneNumber());
        if (model.getClient() != null) {
            dto.setClientId(model.getClient().getId());
            dto.setClientName(model.getClient().getSurname()+" "+model.getClient().getName());
            dto.setPhoneNumber(model.getClient().getPhoneNumber());
        }
        return dto;
    }
}
