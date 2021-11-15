package ru.otus.spring.dao;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.domain.Book;

import java.util.List;

public interface BookDao {
    int count() throws DataAccessException;

    long insertBook(String bookName, long authorId, long genreId) throws DataAccessException;

    void updateBookNameById(long id, String newName) throws DataAccessException;

    Book getBookById(long id) throws DataAccessException;

    List<Book> getAllBooks() throws DataAccessException;

    void deleteByBookId(long id) throws DataAccessException;
}
