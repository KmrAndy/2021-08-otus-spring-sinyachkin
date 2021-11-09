package ru.otus.spring.service;

import ru.otus.spring.domain.Book;
import ru.otus.spring.exception.LibraryAccessException;

import java.util.List;

public interface BookService {
    int getBooksCount() throws LibraryAccessException;

    void addNewBook(long bookId, String bookName, String authorFirstName, String authorLastName,String genreName) throws LibraryAccessException;

    void changeBookNameByBookId(long id, String newName) throws LibraryAccessException;

    Book getBookById(long id) throws LibraryAccessException;

    String getBookInfoById(long id) throws LibraryAccessException;

    List<Book> getAllBooks() throws LibraryAccessException;

    void deleteByBookId(long id) throws LibraryAccessException;
}
