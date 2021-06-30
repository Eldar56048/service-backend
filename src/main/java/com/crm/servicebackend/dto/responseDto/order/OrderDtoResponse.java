package com.crm.servicebackend.dto.responseDto.order;

import com.crm.servicebackend.dto.responseDto.client.ClientDtoResponse;
import com.crm.servicebackend.dto.responseDto.discount.DiscountDtoResponse;
import com.crm.servicebackend.dto.responseDto.model.ModelDtoResponse;
import com.crm.servicebackend.dto.responseDto.orderItem.OrderItemOrderDtoResponse;
import com.crm.servicebackend.dto.responseDto.type.TypeDtoResponse;
import com.crm.servicebackend.dto.responseDto.user.UserItemDtoResponse;
import com.crm.servicebackend.model.Status;
import com.crm.servicebackend.model.TypesOfPayments;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderDtoResponse {
    private Long id;
    private String clientName;
    private String phoneNumber;
    private String problem;
    private List<OrderItemOrderDtoResponse> items;
    private TypeDtoResponse type;
    private ModelDtoResponse model;
    private UserItemDtoResponse acceptedUser;
    private UserItemDtoResponse doneUser;
    private UserItemDtoResponse giveUser;
    private Date acceptedDate;
    private Date gaveDate;
    private Date doneDate;
    private int price;
    private Status status;
    private TypesOfPayments typesOfPayments;
    private Boolean notified;
    private String comment;
    private String modelCompany;
    private ClientDtoResponse client;
    private DiscountDtoResponse discount;
    private String discountName;
    private int discountPercent;
    private String token;
}
