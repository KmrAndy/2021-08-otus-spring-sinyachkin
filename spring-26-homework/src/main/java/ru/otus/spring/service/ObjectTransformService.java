package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.models.*;

import java.util.List;

@Service
public class ObjectTransformService {

    @Autowired
    private ObjectCacheService cacheService;

    public void init(){
        this.cacheService.clearCache();
    }

    public AuthorMongo toAuthorMongo(AuthorJPA author){
        return new AuthorMongo(
                author.getFirstName(),
                author.getLastName(),
                String.valueOf(author.getId()));
    }

    public GenreMongo toGenreMongo(GenreJPA genre){
        return new GenreMongo(
                genre.getName(),
                String.valueOf(genre.getId()));
    }

    public BookMongo toBookMongo(BookJPA book){
        AuthorJPA bookAuthor = book.getAuthor();
        GenreJPA bookGenre = book.getGenre();

        List<AuthorMongo> mongoAuthors =
                List.of(
                        new AuthorMongo(
                                this.cacheService.getAuthorMongoIdCached(bookAuthor.getId()),
                                bookAuthor.getFirstName(),
                                bookAuthor.getLastName(),
                                String.valueOf(bookAuthor.getId())));

        List<GenreMongo> mongoGenres =
                List.of(
                        new GenreMongo(
                                this.cacheService.getGenreMongoIdCached(bookGenre.getId()),
                                bookGenre.getName(),
                                String.valueOf(bookGenre.getId())));

        return new BookMongo(book.getName(),mongoAuthors, mongoGenres, String.valueOf(book.getId()));
    }

    public void cacheAuthors(List<AuthorMongo> mongoAuthors){
        this.cacheService.cacheAuthors(mongoAuthors);
    }

    public void cacheGenres(List<GenreMongo> mongoGenres){
        this.cacheService.cacheGenres(mongoGenres);
    }

    public void cacheBooks(List<BookMongo> mongoBooks){
        this.cacheService.cacheBooks(mongoBooks);
    }
}
