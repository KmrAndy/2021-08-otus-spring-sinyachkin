package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.models.AuthorMongo;


public interface AuthorMongoRepository extends MongoRepository<AuthorMongo, String> {
}
