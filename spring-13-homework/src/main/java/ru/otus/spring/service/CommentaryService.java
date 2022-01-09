package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;

import java.util.List;

public interface CommentaryService {
    Long getBookCommentariesCount(String bookId) throws DataAccessException;

    Commentary addNewCommentary(Book book, String commentaryText) throws DataAccessException;

    void changeCommentaryTextById(String id, String newText) throws DataAccessException;

    void changeCommentariesBook(Book newBook, List<Commentary> commentaries) throws DataAccessException;

    Commentary getCommentaryById(String id) throws DataAccessException;

    String getCommentaryTextById(String id) throws DataAccessException;

    List<Commentary> getCommentariesByBookId(String bookId) throws DataAccessException;

    List<String> getCommentariesTextByBookId(String bookId) throws DataAccessException;

    void deleteByCommentaryId(String id) throws DataAccessException;

    void deleteBookCommentaries(Book book) throws DataAccessException;
}
