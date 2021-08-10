package com.crm.servicebackend.model.enums;

public enum Status {
    DONE{
        @Override
        public String toString() {
            return "Сделано";
        }
    },
    NOTDONE{
        @Override
        public String toString() {
            return "Не сделано";
        }
    },
    GIVEN{
        @Override
        public String toString() {
            return "Выдано";
        }
    },
    WENTCASHIER{
        @Override
        public String toString() {
            return "Ожидается платеж";
        }
    };
}
