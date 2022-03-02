package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;
import ru.otus.spring.model.Card;
import ru.otus.spring.model.User;
import ru.otus.spring.repository.CardRepository;
import ru.otus.spring.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@DisplayName("Репозиторий для работы с картами")
class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Получаем карты владельца")
    @Test
    void shouldReturnCardsByCardHolderId() {
        User user = userRepository.save(new User("Test", "User", "+79992223322", "qwerty"));

        Card firstCard = cardRepository.save(new Card("1234123456785678", "2025-03-01", "000", user));
        Card secondCard = cardRepository.save(new Card("1234123456785679", "2025-04-01", "999", user));

        List<Card> expectedCards = List.of(firstCard, secondCard);
        List<Card> actualCards = cardRepository.getCardsByCardHolderId(user.getId());
        
        assertThat(expectedCards).usingRecursiveComparison().isEqualTo(actualCards);
    }
}