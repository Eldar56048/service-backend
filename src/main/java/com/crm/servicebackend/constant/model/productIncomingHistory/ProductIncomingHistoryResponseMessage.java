package com.crm.servicebackend.constant.model.productIncomingHistory;

public class ProductIncomingHistoryResponseMessage {
    public static String PRODUCT_NOT_ENOUGH_IN_STORAGE_MESSAGE(Long historyId, int quantity) {
        return "Невозможно удалить историю добавление №"+historyId+" так как на складе недостаточно товаров. Нужно еще "+quantity;
    }
    public static String PRODUCT_INCOMING_HISTORY_NOT_FOUND_MESSAGE(Long incomingHistoryId) {
        return "История добавление товара с идентификатором № "+incomingHistoryId+" не найдено";
    }
}
