package ru.otus.spring.repositories;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;

import java.util.List;

@RequiredArgsConstructor
public class CommentaryRepositoryCustomImpl implements CommentaryRepositoryCustom{
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<UpdateResult> updateCommentariesBookByBook(Book book) throws DataAccessException{
        Query query = new Query();
        query.addCriteria(Criteria.where("book.id").is(book.getId()));

        Update update = new Update();
        update.set("book", book);

        return mongoTemplate.updateMulti(query, update, Commentary.class);
    }

    @Override
    public Flux<Commentary> findAllByBook(String bookId) throws DataAccessException{
        Query query = new Query();
        query.addCriteria(Criteria.where("book.id").is(bookId));

        return mongoTemplate.find(query, Commentary.class);
    }

    @Override
    public Mono<DeleteResult> deleteCommentariesByBookId(String bookId) throws DataAccessException{
        Query query = new Query();
        query.addCriteria(Criteria.where("book.id").is(bookId));
        return mongoTemplate.remove(query, Commentary.class);
    }
}
