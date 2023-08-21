package ru.otus.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.model.Card;
import ru.otus.spring.model.User;
import ru.otus.spring.rest.dto.CardDto;
import ru.otus.spring.service.CardService;
import ru.otus.spring.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
@DisplayName("Тестирование рест контроллера карт")
class CardControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CardService cardService;

    @MockBean
    private UserService userService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final User expectedUser = new User("1", "Test", "User", "+79992223311", "qwerty");
    private final Card expectedCard = new Card("1","1234123456785678", "2025-03-01", "000", expectedUser);
    private final CardDto expectedCardDto = CardDto.convertToDto(expectedCard);

    @WithMockUser(value = "user", authorities = {"ROLE_USER"})
    @DisplayName("Получаем карту по id")
    @Test
    void shouldReturnExpectedCardById() throws Exception {
        when(cardService.getCardById(expectedCard.getId())).thenReturn(expectedCard);

        mvc.perform(get("/api/cards/id/" + expectedCard.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedCardDto.getId()))
                .andExpect(jsonPath("$.number").value(expectedCardDto.getNumber()))
                .andExpect(jsonPath("$.cardHolderId").value(expectedCardDto.getCardHolderId()));
    }

    @WithMockUser(value = "user", authorities = {"ROLE_USER"})
    @DisplayName("Получаем список карт по id пользователя")
    @Test
    void shouldReturnCardsByCardHolderId() throws Exception {
        when(cardService.getCardsByCardHolderId(expectedUser.getId())).thenReturn(List.of(expectedCard));

        mvc.perform(get("/api/cards/cardholder/" + expectedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedCardDto.getId()))
                .andExpect(jsonPath("$[0].number").value(expectedCardDto.getNumber()))
                .andExpect(jsonPath("$[0].cardHolderId").value(expectedCardDto.getCardHolderId()));
    }

    @WithMockUser(value = "user", authorities = {"ROLE_USER"})
    @DisplayName("Удаляем карту")
    @Test
    void shouldDeleteCard() throws Exception {
        ArgumentCaptor<String> idCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(cardService).deleteCard(idCapture.capture());

        mvc.perform(delete("/api/cards/delete/id/" + expectedCard.getId()))
                .andExpect(status().isOk());

        assertEquals(expectedCard.getId(), idCapture.getValue());
    }

    @WithMockUser(value = "user", authorities = {"ROLE_USER"})
    @DisplayName("Добавляем карту")
    @Test
    void shouldAddCard() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestBody = ow.writeValueAsString(expectedCard);

        when(cardService
                .addCard(
                        expectedCard.getNumber(),expectedCard.getExpiryDate(),
                        expectedCard.getCvv(), expectedUser.getId()))
                .thenReturn(expectedCard);

        mvc.perform(post("/api/cards/add/cardholder/" + expectedUser.getId()).contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedCardDto.getId()))
                .andExpect(jsonPath("$.number").value(expectedCardDto.getNumber()))
                .andExpect(jsonPath("$.cardHolderId").value(expectedCardDto.getCardHolderId()));
    }
}