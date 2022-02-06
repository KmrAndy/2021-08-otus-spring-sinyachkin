package ru.otus.spring.repositories;

import com.mongodb.client.result.DeleteResult;
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
    public Flux<Commentary> updateCommentariesBook(Book newBook, List<Commentary> commentaries) throws DataAccessException{
        for(Commentary comm : commentaries){
            comm.setBook(newBook);
            mongoTemplate.save(comm);
        }
        return Flux.fromIterable(commentaries);
    }

    @Override
    public Flux<Commentary> findAllByBook(String bookId) throws DataAccessException{
        Query query = new Query();
        query.addCriteria(Criteria.where("book.id").is(bookId));

        return mongoTemplate.find(query, Commentary.class);
    }

    @Override
    public Mono<DeleteResult> deleteCommentariesByBook(Book book) throws DataAccessException{
        Query query = new Query();
        query.addCriteria(Criteria.where("book").is(book));
        return mongoTemplate.remove(query, Commentary.class);
    }
}
