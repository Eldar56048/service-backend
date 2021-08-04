package com.crm.servicebackend.constant.response.storage;

public class StorageResponseMessage {
    public static String PRODUCT_STORAGE_NOT_ENOUGH_MESSAGE(int quantity) {
        return "Не хватает товаров на складе. Нужно "+quantity+"шт";
    }
}
