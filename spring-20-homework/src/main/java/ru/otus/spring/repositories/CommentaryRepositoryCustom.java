package ru.otus.spring.repositories;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.dao.DataAccessException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;

import java.util.List;

public interface CommentaryRepositoryCustom {

    Mono<UpdateResult> updateCommentariesBookByBook(Book book) throws DataAccessException;

    Flux<Commentary> findAllByBook(String bookId) throws DataAccessException;

    Mono<DeleteResult> deleteCommentariesByBookId(String bookId) throws DataAccessException;
}
