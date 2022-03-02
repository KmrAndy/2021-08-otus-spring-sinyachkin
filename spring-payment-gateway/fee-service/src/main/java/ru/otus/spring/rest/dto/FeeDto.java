package ru.otus.spring.rest.dto;

import ru.otus.spring.model.Fee;

public class FeeDto {
    private String id;

    private double minAmount;

    private double percentValue;

    public FeeDto(String id, double minAmount, double percentValue) {
        this.id = id;
        this.minAmount = minAmount;
        this.percentValue = percentValue;
    }

    public static FeeDto convertToDto(Fee fee) {
        return new FeeDto(fee.getId(), fee.getMinAmount().doubleValue(), fee.getPercentValue());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public double getPercentValue() {
        return percentValue;
    }

    public void setPercentValue(double percentValue) {
        this.percentValue = percentValue;
    }
}
