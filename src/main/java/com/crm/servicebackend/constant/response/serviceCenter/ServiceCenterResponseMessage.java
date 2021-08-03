package com.crm.servicebackend.constant.response.serviceCenter;

public class ServiceCenterResponseMessage {
    public static final String SERVICE_CENTER_TWO_ANOTHER_ID_MESSAGE = "Два разных id";
    public static final String SERVICE_CENTER_EXISTS_BY_NAME_MESSAGE = "Сервисный центр с таким названием уже существует";

    public static String SERVICE_CENTER_NOT_FOUND_MESSAGE(Long serviceCenterId) {
        return "Сервисный центр с идентификатором № "+serviceCenterId+" не найдено";
    }
}
