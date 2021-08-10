package com.crm.servicebackend.model.enums;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    MODERATOR,
    ADMIN,
    CASHIER,
    OWNER;
    @Override
    public String getAuthority() {
        return name();
    }
}
