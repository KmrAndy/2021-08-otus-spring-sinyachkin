package ru.otus.spring.repositories;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;

import java.util.List;

public interface CommentaryRepositoryCustom {
    Long countByBook(String bookId) throws DataAccessException;

    int updateTextById(String id, String newText) throws DataAccessException;

    void updateCommentariesBook(Book newBook, List<Commentary> commentaries) throws DataAccessException;

    List<Commentary> findAllByBook(String bookId) throws DataAccessException;

    void deleteCommentariesByBook(Book book) throws DataAccessException;
}
