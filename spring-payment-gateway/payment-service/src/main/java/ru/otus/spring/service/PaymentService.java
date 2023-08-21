package ru.otus.spring.service;

import ru.otus.spring.model.Incoming;
import ru.otus.spring.model.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> addPayment(Incoming incoming);
}
