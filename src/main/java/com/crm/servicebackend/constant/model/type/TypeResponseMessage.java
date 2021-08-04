package com.crm.servicebackend.constant.model.type;

public class TypeResponseMessage {

    public static final String TYPE_TWO_ANOTHER_ID_MESSAGE = "Два разных id";
    public static final String TYPE_EXISTS_BY_NAME_MESSAGE = "Тип устройства с таким названием уже существует";

    public static final String TYPE_NOT_FOUND_MESSAGE(Long typeId) {
        return "Тип устройства с идентификатором № "+typeId+" не найдено";
    }

}
