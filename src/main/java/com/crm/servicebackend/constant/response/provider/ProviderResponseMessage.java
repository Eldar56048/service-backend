package com.crm.servicebackend.constant.response.provider;

public class ProviderResponseMessage {
    public static final String PROVIDER_TWO_ANOTHER_ID_MESSAGE = "Два разных id";
    public static final String PROVIDER_EXISTS_BY_NAME_MESSAGE = "Поставщик с таким именем уже существует";

    public static String PROVIDER_NOT_FOUND_MESSAGE(Long providerId) {
        return "Поставщик с идентификатором № "+providerId+" не найдено";
    }
}
