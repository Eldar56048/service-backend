package com.crm.servicebackend.constant.response.orderItem;

public class OrderItemResponseMessage {
    public static String ORDER_ITEM_NOT_FOUND_MESSAGE(Long itemId) {
        return "Элемент заказа с идентификатором № "+itemId+" не найдено";
    }
}
