package com.crm.servicebackend.constant.response.order;

public class OrderResponseMessage {
    public static final String ORDER_NOT_NOTIFIED_MESSAGE = "Клиент не уведомлен";
    public static final String ORDER_ALREADY_NOTIFIED_MESSAGE = "Клиент уже уведомлен";

    public static String ORDER_STATUS_NOT_CHANGED_MESSAGE(String status) {
        return "Невозможно изменить статус заказа. Так как статус заказа "+status;
    }

    public static String ORDER_NOT_FOUND_MESSAGE(Long orderId) {
        return "Заказ с идентификатором № "+orderId+" не найдено";
    }
}
