package ru.otus.spring.repositories;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;

import java.util.List;


public interface CommentaryRepository {
    Long count(Book book) throws DataAccessException;

    Commentary insertCommentary(Commentary commentary) throws DataAccessException;

    void updateCommentaryById(long id, String newText) throws DataAccessException;

    Commentary getCommentaryById(long id) throws DataAccessException;

    List<Commentary> getCommentariesByBookId(Book book) throws DataAccessException;

    void deleteByCommentaryId(long id) throws DataAccessException;
}
