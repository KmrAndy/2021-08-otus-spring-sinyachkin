package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import ru.otus.spring.model.Card;
import ru.otus.spring.model.User;
import ru.otus.spring.repository.CardRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest()
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@DisplayName("Сервис для работы с картами")
class CardServiceImplTest {

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CardServiceImpl cardService;

    private final User expectedUser = new User("1", "Test", "User", "+79992223311", "qwerty");
    private final Card expectedCard = new Card("1","1234123456785678", "2025-03-01", "000", expectedUser);

    @DisplayName("Добавляем пользователя")
    @Test
    void shouldAddNewUser() {
        Card card = new Card(
                expectedCard.getNumber(), expectedCard.getExpiryDate(),
                expectedCard.getCvv(), expectedCard.getCardHolder()
        );

        when(cardRepository.save(card)).thenReturn(expectedCard);
        when(userService.getUserById(card.getCardHolder().getId())).thenReturn(card.getCardHolder());
        when(bCryptPasswordEncoder.encode(card.getCvv())).thenReturn(card.getCvv());

        Card actualCard = cardService.addCard(card.getNumber(), card.getExpiryDate(), card.getCvv(), card.getCardHolder().getId());

        assertThat(actualCard).isEqualTo(expectedCard);
    }

    @DisplayName("Удаляем карту по id")
    @Test
    void shouldDeleteCard() {
        ArgumentCaptor<String> cardCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(cardRepository).deleteById(cardCapture.capture());

        cardService.deleteCard(expectedCard.getId());
        assertEquals(expectedCard.getId(), cardCapture.getValue());
    }

    @DisplayName("Получаем карту по id")
    @Test
    void shouldReturnCardById() {
        when(cardRepository.findById(expectedCard.getId())).thenReturn(Optional.of(expectedCard));
        assertEquals(cardService.getCardById(expectedCard.getId()), expectedCard);
    }

    @DisplayName("Получаем список карт владельца")
    @Test
    void shouldReturnCardsByCardHolderId() {
        when(cardRepository.getCardsByCardHolderId(expectedUser.getId())).thenReturn(List.of(expectedCard));
        assertEquals(cardService.getCardsByCardHolderId(expectedUser.getId()), List.of(expectedCard));
    }
}