package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.model.Payment;

public interface PaymentRepository extends MongoRepository<Payment, String> {
}
