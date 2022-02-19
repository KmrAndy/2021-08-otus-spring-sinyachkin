package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.models.AuthorMongo;
import ru.otus.spring.models.BookMongo;
import ru.otus.spring.models.GenreMongo;

import java.util.HashMap;
import java.util.List;

@Service
public class ObjectCacheService {
    private HashMap<String, HashMap<Long, String>> cache = new HashMap<>();

    private static final String AUTHOR_KEY = "author";
    private static final String GENRE_KEY = "genre";
    private static final String BOOK_KEY = "book";

    public void clearCache(){
        this.cache.clear();
    }

    public String getAuthorMongoIdCached(Long authorId){
        return this.cache
                .get(AUTHOR_KEY)
                .get(authorId);
    }

    public String getGenreMongoIdCached(Long genreId){
        return this.cache
                .get(GENRE_KEY)
                .get(genreId);
    }

    public String getBookMongoIdCached(Long bookId){
        return this.cache
                .get(BOOK_KEY)
                .get(bookId);
    }

    public void cacheAuthors(List<AuthorMongo> mongoAuthors){
        HashMap<Long, String> idMap = new HashMap<>();
        for (AuthorMongo author : mongoAuthors) {
            idMap.put(Long.valueOf(author.getJPADbId()), author.getId());
        }
        this.cache.put(AUTHOR_KEY, idMap);
    }

    public void cacheGenres(List<GenreMongo> mongoGenres){
        HashMap<Long, String> idMap = new HashMap<>();
        for (GenreMongo genre : mongoGenres) {
            idMap.put(Long.valueOf(genre.getJPADbId()), genre.getId());
        }
        this.cache.put(GENRE_KEY, idMap);
    }

    public void cacheBooks(List<BookMongo> mongoBooks){
        HashMap<Long, String> idMap = new HashMap<>();
        for (BookMongo book : mongoBooks) {
            idMap.put(Long.valueOf(book.getJPADbId()), book.getId());
        }
        this.cache.put(BOOK_KEY, idMap);
    }

}
