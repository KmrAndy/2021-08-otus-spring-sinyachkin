package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.models.GenreMongo;

public interface GenreMongoRepository extends MongoRepository<GenreMongo, String> {
}
