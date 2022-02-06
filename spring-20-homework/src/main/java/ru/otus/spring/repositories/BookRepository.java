package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.spring.models.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
}
