package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;

public interface CommentaryRepository extends ReactiveMongoRepository<Commentary, String>, CommentaryRepositoryCustom {
    Flux<Commentary> findAllByBook(Book book);
}
