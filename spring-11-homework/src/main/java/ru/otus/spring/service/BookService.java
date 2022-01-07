package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.models.Book;

import java.util.List;

public interface BookService {
    long getBooksCount() throws DataAccessException;

    Book addNewBook(String bookName, String authorFirstName, String authorLastName,String genreName) throws DataAccessException;

    void changeBookNameByBookId(Long id, String newName);

    Book getBookById(Long id) throws DataAccessException;

    String getBookInfoById(Long id) throws DataAccessException;

    List<Book> getAllBooks() throws DataAccessException;

    void deleteByBookId(Long id) throws DataAccessException;
}
