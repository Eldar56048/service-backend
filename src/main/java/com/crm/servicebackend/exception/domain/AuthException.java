package com.crm.servicebackend.exception.domain;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AuthException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private String code;
    public AuthException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
