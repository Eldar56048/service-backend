package com.crm.servicebackend.constant.model.discount;

public class DiscountResponseMessage {
    public static final String DISCOUNT_TWO_ANOTHER_ID_MESSAGE = "Два разных id";
    public static final String DISCOUNT_EXISTS_BY_NAME_MESSAGE = "Скидка с таким названием уже существует";
    public static final String DISCOUNT_EXISTS_BY_PERCENTAGE_MESSAGE = "Скидка с таким процентом уже существует";

    public static final String DISCOUNT_NOT_FOUND_MESSAGE(Long discountId) {
        return "Скидка с идентификатором № "+discountId+" не найдено";
    }
}
