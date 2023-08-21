package ru.otus.spring.model;

import java.math.BigDecimal;

public class Incoming {

    private String cardNumber;

    private BigDecimal operationAmount;

    private BigDecimal feeAmount;

    private String description;

    public Incoming(String cardNumber, BigDecimal operationAmount, BigDecimal feeAmount, String description) {
        this.cardNumber = cardNumber;
        this.operationAmount = operationAmount;
        this.feeAmount = feeAmount;
        this.description = description;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public BigDecimal getOperationAmount() {
        return operationAmount;
    }

    public void setOperationAmount(BigDecimal operationAmount) {
        this.operationAmount = operationAmount;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
