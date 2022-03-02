package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.model.Card;

import java.util.List;

public interface CardRepository extends MongoRepository<Card, String> {
    List<Card> getCardsByCardHolderId(String cardHolderId);
}
