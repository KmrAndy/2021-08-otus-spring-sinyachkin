package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.spring.models.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {
}
