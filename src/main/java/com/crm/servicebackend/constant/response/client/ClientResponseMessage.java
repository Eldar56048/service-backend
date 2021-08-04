package com.crm.servicebackend.constant.response.client;

public class ClientResponseMessage {
    public static final String CLIENT_TWO_ANOTHER_ID_MESSAGE = "Два разных id";

    public static String CLIENT_NOT_FOUND_MESSAGE(Long clientId) {
        return "Клиент с идентификатором № "+clientId+" не найдено";
    }
}
