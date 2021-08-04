package com.crm.servicebackend.constant;

import com.crm.servicebackend.model.Order;

public class ConstantVariables {
    public static final String STANDARD_DISCOUNT_NAME = "Стандартный";
    public static final String DEFAULT_CLIENT_SURNAME = "Клиент";

    public static String ORDER_NOTIFY_MESSAGE(Order order) {
        return "Ваш заказ №" + order.getId() + " готов \n" +
                "Кто сделал: " + order.getDoneUser().getSurname() + " " + order.getDoneUser().getName() + "\n" +
                "Тел: " + order.getDoneUser().getPhoneNumber() + "\n" +
                "Цена: " + order.getPrice() + "\n" +
                "С уважением команда "+order.getServiceCenter().getName();
    }
}
