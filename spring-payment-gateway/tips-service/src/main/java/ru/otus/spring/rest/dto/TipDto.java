package ru.otus.spring.rest.dto;

import ru.otus.spring.model.Tip;

import java.math.BigDecimal;

public class TipDto {
    private String id;

    private String fromFullName;

    private String fromCard;

    private String toFullName;

    private BigDecimal tipAmount;

    private BigDecimal feeAmount;

    private BigDecimal fullAmount;

    public TipDto(String id, String fromFullName, String fromCard, String toFullName,
                  BigDecimal tipAmount, BigDecimal feeAmount, BigDecimal fullAmount) {
        this.id = id;
        this.fromFullName = fromFullName;
        this.fromCard = fromCard;
        this.toFullName = toFullName;
        this.tipAmount = tipAmount;
        this.feeAmount = feeAmount;
        this.fullAmount = fullAmount;
    }

    public static TipDto convertToDto(Tip tip) {
        return new TipDto(
                        tip.getId(),
                        tip.getFromUser().getFullName(),
                        getMaskedNumber(tip.getFromCard()),
                        tip.getToWaiter().getFullName(),
                        tip.getTipAmount(),
                        tip.getFeeAmount(),
                        tip.getFullAmount());
    }

    private static String getMaskedNumber(String number){
        return number.substring(0, 4) + "********" + number.substring((number.length() - 4));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromFullName() {
        return fromFullName;
    }

    public void setFromFullName(String fromFullName) {
        this.fromFullName = fromFullName;
    }

    public String getFromCard() {
        return fromCard;
    }

    public void setFromCard(String fromCard) {
        this.fromCard = fromCard;
    }

    public String getToFullName() {
        return toFullName;
    }

    public void setToFullName(String toFullName) {
        this.toFullName = toFullName;
    }

    public BigDecimal getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(BigDecimal tipAmount) {
        this.tipAmount = tipAmount;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public BigDecimal getFullAmount() {
        return fullAmount;
    }

    public void setFullAmount(BigDecimal fullAmount) {
        this.fullAmount = fullAmount;
    }
}
