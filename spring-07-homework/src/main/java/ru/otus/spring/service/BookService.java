package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.domain.Book;

import java.util.List;

public interface BookService {
    int getBooksCount() throws DataAccessException;

    long addNewBook(String bookName, String authorFirstName, String authorLastName,String genreName) throws DataAccessException;

    void changeBookNameByBookId(long id, String newName) throws DataAccessException;

    Book getBookById(long id) throws DataAccessException;

    String getBookInfoById(long id) throws DataAccessException;

    List<Book> getAllBooks() throws DataAccessException;

    void deleteByBookId(long id) throws DataAccessException;
}
