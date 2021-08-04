package com.crm.servicebackend.constant.response.user;

public class UserResponseMessage {
    public static final String USER_TWO_ANOTHER_ID_MESSAGE = "Два разных id";
    public static final String USER_EXISTS_BY_USERNAME_MESSAGE = "Пользователь с таким логином уже существует";

    public static String USER_NOT_FOUND_MESSAGE(Long userId) {
        return "Пользователь с идентификатором № "+userId+" не найдено";
    }
}
