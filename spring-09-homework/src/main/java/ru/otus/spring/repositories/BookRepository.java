package ru.otus.spring.repositories;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.models.Book;

import java.util.List;

public interface BookRepository {
    Long count() throws DataAccessException;

    long insertBook(Book book) throws DataAccessException;

    void updateBookNameById(long id, String newName) throws DataAccessException;

    Book getBookById(long id) throws DataAccessException;

    List<Book> getAllBooks() throws DataAccessException;

    void deleteByBookId(long id) throws DataAccessException;
}
