package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.model.Waiter;

import java.util.Optional;

public interface WaiterRepository extends MongoRepository<Waiter, String> {
    Optional<Waiter> findWaiterByPhone(String phone);
}
