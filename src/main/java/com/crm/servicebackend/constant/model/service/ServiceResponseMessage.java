package com.crm.servicebackend.constant.model.service;

public class ServiceResponseMessage {
    public static final String SERVICE_TWO_ANOTHER_ID_MESSAGE = "Два разных id";
    public static final String SERVICE_EXISTS_BY_NAME_MESSAGE = "Услуга с таким наименованием уже существует";

    public static String SERVICE_NOT_FOUND_MESSAGE(Long serviceId) {
        return "Услуга с идентификатором № "+serviceId+" не найдено";
    }
}
