package ru.otus.spring.repositories;

import com.mongodb.client.result.DeleteResult;
import org.springframework.dao.DataAccessException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;

import java.util.List;

public interface CommentaryRepositoryCustom {

    Flux<Commentary> updateCommentariesBook(Book newBook, List<Commentary> commentaries) throws DataAccessException;

    Flux<Commentary> findAllByBook(String bookId) throws DataAccessException;

    Mono<DeleteResult> deleteCommentariesByBook(Book book) throws DataAccessException;
}
