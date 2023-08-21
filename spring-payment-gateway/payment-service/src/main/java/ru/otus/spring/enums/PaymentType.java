package ru.otus.spring.enums;

public enum PaymentType {
    FEE("fee"),
    GENERAL("general");

    private final String code;

    PaymentType(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }
}
