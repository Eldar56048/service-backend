package com.crm.servicebackend.constant.model.product;

public class ProductResponseMessage {
    public static final String PRODUCT_TWO_ANOTHER_ID_MESSAGE = "Два разных id";
    public static final String PRODUCT_EXISTS_BY_NAME_MESSAGE = "Товар с таким наименованием уже существует";

    public static String PRODUCT_NOT_FOUND_MESSAGE(Long productId) {
        return "Товар с идентификатором № "+productId+" не найдено";
    }
}
