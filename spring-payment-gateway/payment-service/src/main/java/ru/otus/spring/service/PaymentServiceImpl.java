package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.enums.PaymentType;
import ru.otus.spring.exception.OtherAccessException;
import ru.otus.spring.model.Incoming;
import ru.otus.spring.model.Payment;
import ru.otus.spring.repository.PaymentRepository;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public List<Payment> addPayment(Incoming incoming){
        List<Payment> payments = new ArrayList<>();
        LocalDateTime dateTime = LocalDateTime.now();

        if(incoming.getOperationAmount().doubleValue() != 0.0){
            try {
                payments.add(paymentRepository.save(
                        new Payment(
                                PaymentType.GENERAL,
                                incoming.getCardNumber(),
                                dateTime,
                                incoming.getOperationAmount().setScale(2, RoundingMode.HALF_UP),
                                incoming.getDescription())));
            } catch (DataAccessException e) {
                throw new OtherAccessException(e);
            }
        }

        if(incoming.getFeeAmount().doubleValue() != 0.0){
            try {
                payments.add(paymentRepository.save(
                        new Payment(
                                PaymentType.FEE,
                                incoming.getCardNumber(),
                                dateTime,
                                incoming.getFeeAmount().setScale(2, RoundingMode.HALF_UP),
                                incoming.getDescription())));
            } catch (DataAccessException e) {
                throw new OtherAccessException(e);
            }
        }

        return payments;
    }
}
