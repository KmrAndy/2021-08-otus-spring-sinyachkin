package ru.otus.spring.dao;

import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.LibraryAccessException;

import java.util.List;

public interface LibraryDao {
    int count() throws LibraryAccessException;

    void insertBook(Book book) throws LibraryAccessException;

    void updateBookNameById(long id, String newName) throws LibraryAccessException;

    Book getBookById(long id) throws LibraryAccessException;

    Author getAuthorByFullName(String firstName, String lastName) throws LibraryAccessException;

    Genre getGenreByName(String genreName) throws LibraryAccessException;

    List<Book> getAllBooks() throws LibraryAccessException;

    void deleteByBookId(long id) throws LibraryAccessException;
}
