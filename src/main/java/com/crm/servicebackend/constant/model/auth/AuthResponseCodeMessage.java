package com.crm.servicebackend.constant.model.auth;

public class AuthResponseCodeMessage {
    public static final String TOKEN_EXPIRED_MESSAGE = "Срок действия токена истек";
    public static final String FETCH_USERNAME_FROM_TOKEN_ERROR_MESSAGE = "Произошла ошибка при получении имени пользователя из токена";
    public static final String INVALID_USERNAME_OR_PASSWORD_MESSAGE = "Ошибка аутентификации. Имя пользователя или пароль недействительны";
    public static final String INVALID_TOKEN_MESSAGE = "Неверный формат пользовательского токена";
    public static final String NO_TOKEN_MESSAGE = "Не удалось найти токен";
    public static final String SERVICE_CENTER_NOT_ACTIVE_MESSAGE = "Ваш сервисный центр не активен";
    public static final String ACCOUNT_NOT_ACTIVE_MESSAGE = "Ваш аккаунт не активен";
    public static final String SOMETHING_WENT_WRONG_MESSAGE = "Что-то пошло не так";
}
