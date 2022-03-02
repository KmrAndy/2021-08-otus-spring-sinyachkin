package ru.otus.spring.service;

import ru.otus.spring.model.Tip;
import ru.otus.spring.model.User;
import ru.otus.spring.model.Waiter;

import java.math.BigDecimal;
import java.util.List;

public interface TipService {
    Tip addTip(User fromUser, String fromCard, Waiter toWaiter, BigDecimal tipAmount);

    List<Tip> getTipsByFromUserId(String fromUserId);
}
