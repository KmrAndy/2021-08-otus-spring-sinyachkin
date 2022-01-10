package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;

import java.util.List;

public interface BookService {
    long getBooksCount() throws DataAccessException;

    Book addNewBook(String bookName, String authorFirstName, String authorLastName, String genreName) throws DataAccessException;

    void changeBookNameByBookId(String id, String newName);

    Book getBookById(String id) throws DataAccessException;

    String getBookInfoById(String id) throws DataAccessException;

    List<Book> getAllBooks() throws DataAccessException;

    void deleteByBookId(String id) throws DataAccessException;

    Commentary addBookCommentary(String bookId, String commentaryText) throws DataAccessException;
}
