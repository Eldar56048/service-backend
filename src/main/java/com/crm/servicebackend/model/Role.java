package com.crm.servicebackend.model;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER{
        @Override
        public String toString() {
            return "Мастер";
        }
    },
    MODERATOR{
        @Override
        public String toString() {
            return "Модератор";
        }
    },
    ADMIN{
        @Override
        public String toString() {
            return "Админ";
        }
    },
    CASHIER{
        @Override
        public String toString() {
            return "Кассир";
        }
    };
    @Override
    public String getAuthority() {
        return name();
    }
}
