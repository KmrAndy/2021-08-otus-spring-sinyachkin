package ru.otus.spring.rest;

import org.springframework.web.bind.annotation.*;
import ru.otus.spring.model.Card;
import ru.otus.spring.rest.dto.CardDto;
import ru.otus.spring.service.CardService;
import ru.otus.spring.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/cards")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping(value = "/id/{id}")
    public CardDto getCardById(@PathVariable String id) {
        return CardDto.convertToDto(cardService.getCardById(id));
    }

    @GetMapping(value = "/cardholder/{id}")
    public List<CardDto> getCardsByCardHolderId(@PathVariable(name = "id") String cardHolderId) {
        return cardService.getCardsByCardHolderId(cardHolderId)
                .stream()
                .map(CardDto::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/add/cardholder/{id}")
    public CardDto addCard(@PathVariable(name = "id") String cardHolderId, @RequestBody Card card) {
        return CardDto.convertToDto(
                cardService.addCard(
                        card.getNumber(),
                        card.getExpiryDate(),
                        card.getCvv(),
                        cardHolderId));
    }

    @DeleteMapping(value = "/delete/id/{id}")
    public void deleteCard(@PathVariable String id) {
        cardService.deleteCard(id);
    }
}
