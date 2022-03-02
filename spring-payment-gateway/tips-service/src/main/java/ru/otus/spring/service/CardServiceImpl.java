package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.NoCardFoundException;
import ru.otus.spring.exception.OtherAccessException;
import ru.otus.spring.model.Card;
import ru.otus.spring.repository.CardRepository;

import java.util.List;

@Service
public class CardServiceImpl implements CardService{
    private final CardRepository cardRepository;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public CardServiceImpl(CardRepository cardRepository, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.cardRepository = cardRepository;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public Card addCard(String number, String expiryDate, String cvv, String cardHolderId){
        try {
            return cardRepository.save(
                    new Card(number, expiryDate, bCryptPasswordEncoder.encode(cvv), userService.getUserById(cardHolderId)));
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    @Transactional
    public void deleteCard(String id){
        try {
            cardRepository.deleteById(id);
        } catch (DataAccessException e){
            throw new OtherAccessException(e);
        }
    }

    public Card getCardById(String id){
        return cardRepository.findById(id)
                .orElseThrow(() -> new NoCardFoundException("Card not found"));
    }

    public List<Card> getCardsByCardHolderId(String cardHolderId){
        try {
            return cardRepository.getCardsByCardHolderId(cardHolderId);
        } catch (DataAccessException e){
            throw new OtherAccessException(e);
        }
    }
}
