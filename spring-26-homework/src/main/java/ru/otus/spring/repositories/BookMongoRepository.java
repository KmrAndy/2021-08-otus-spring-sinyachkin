package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.models.BookMongo;

public interface BookMongoRepository extends MongoRepository<BookMongo, String> {
}

