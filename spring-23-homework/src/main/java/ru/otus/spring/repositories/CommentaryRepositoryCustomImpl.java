package ru.otus.spring.repositories;

import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;

import java.util.List;

@RequiredArgsConstructor
public class CommentaryRepositoryCustomImpl implements CommentaryRepositoryCustom{
    private final MongoTemplate mongoTemplate;

    @Override
    public Long countByBook(String bookId) throws DataAccessException{
        Query query = new Query();
        query.addCriteria(Criteria.where("book.id").is(bookId));
        return mongoTemplate.count(query, Commentary.class);
    }

    @Override
    public int updateTextById(String id, String newText) throws DataAccessException{
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("id").is(id));
        update.set("text", newText);
        Commentary commentary = mongoTemplate.findAndModify(query, update, Commentary.class);

        if (commentary == null){
            return 0;
        }
        return 1;
    }

    @Override
    public void updateCommentariesBook(Book newBook, List<Commentary> commentaries) throws DataAccessException{
        for(Commentary comm : commentaries){
            comm.setBook(newBook);
            mongoTemplate.save(comm);
        }
    }

    @Override
    public List<Commentary> findAllByBook(String bookId) throws DataAccessException{
        Query query = new Query();
        query.addCriteria(Criteria.where("book.id").is(bookId));

        return mongoTemplate.find(query, Commentary.class);
    }

    @Override
    public void deleteCommentariesByBook(Book book) throws DataAccessException{
        Query query = new Query();
        query.addCriteria(Criteria.where("book").is(book));
        mongoTemplate.remove(query, Commentary.class);
    }
}
