package com.crm.servicebackend.constant.model.discount;

public class DiscountValidationConstants {
    public static final String FIELD_DISCOUNT_NAME_REQUIRED_MESSAGE = "Поле название скидки обязательно.";
    public static final String FIELD_DISCOUNT_NAME_LENGTH_MESSAGE = "Длина название скидки должна быть больше нуля";
    public static final String FIELD_PERCENTAGE_REQUIRED_MESSAGE = "Поле процент обязательно.";
    public static final String FIELD_PERCENTAGE_MIN_VALUE_MESSAGE = "Значение поле должно быть больше 0";
    public static final String FIELD_PERCENTAGE_MAX_VALUE_MESSAGE = "Значение поле должно быть меньше 100";
    public static final String FIELD_ID_REQUIRED_MESSAGE = "Поле id обязательно";
    public static final String FIELD_ID_CANNOT_BE_NEGATIVE_MESSAGE = "id не может быть негативным числом";
}
