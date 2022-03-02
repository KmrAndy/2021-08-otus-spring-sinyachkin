package ru.otus.spring.service;

import ru.otus.spring.model.Card;
import ru.otus.spring.model.User;

import java.util.List;

public interface CardService {
    Card addCard(String number, String expiryDate, String cvv, String cardHolderId);

    void deleteCard(String id);

    Card getCardById(String id);

    List<Card> getCardsByCardHolderId(String cardHolderId);
}
