package com.crm.servicebackend.constant.response.model;

public class ModelResponseMessage {

    public static final String MODEL_TWO_ANOTHER_ID_MESSAGE = "Два разных id";
    public static final String MODEL_EXISTS_BY_NAME_MESSAGE = "Модель с таким названием уже существует";

    public static final String MODEL_NOT_FOUND_MESSAGE(Long modelId) {
        return "Модель с идентификатором № "+modelId+" не найдено";
    }
}
