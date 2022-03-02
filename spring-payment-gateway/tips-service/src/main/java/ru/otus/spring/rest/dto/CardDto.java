package ru.otus.spring.rest.dto;

import ru.otus.spring.model.Card;

public class CardDto {
    private String id;

    private String number;

    private String cardHolderId;

    public CardDto(String id, String number, String cardHolderId) {
        this.id = id;
        this.number = number;
        this.cardHolderId = cardHolderId;
    }

    public static CardDto convertToDto(Card card) {
        return new CardDto(card.getId(), getMaskedNumber(card.getNumber()), card.getCardHolder().getId());
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCardHolderId() {
        return cardHolderId;
    }

    public void setCardHolderId(String cardHolderId) {
        this.cardHolderId = cardHolderId;
    }
}
