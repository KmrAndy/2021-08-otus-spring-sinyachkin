package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.model.Fee;

import java.util.Optional;

public interface FeeRepository extends MongoRepository<Fee, String> {
    Optional<Fee> findFeeByBin(String bin);
}
