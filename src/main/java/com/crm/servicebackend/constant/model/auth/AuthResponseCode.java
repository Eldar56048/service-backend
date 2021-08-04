package com.crm.servicebackend.constant.model.auth;

public class AuthResponseCode {
    public static final String TOKEN_EXPIRED_CODE = "auth/token-expired";
    public static final String FETCH_USERNAME_FROM_TOKEN_ERROR_CODE = "fetch-user/error-token";
    public static final String INVALID_USERNAME_OR_PASSWORD_CODE = "auth/invalid-username-and-password";
    public static final String INVALID_TOKEN_CODE = "auth/invalid-token";
    public static final String NO_TOKEN_CODE = "auth/not-token";
    public static final String SERVICE_CENTER_NOT_ACTIVE_CODE = "service-center/not-active-token";
    public static final String ACCOUNT_NOT_ACTIVE_CODE = "user/not-active";
    public static final String SOMETHING_WENT_WRONG_CODE = "auth/smth-went-wrong";
}
