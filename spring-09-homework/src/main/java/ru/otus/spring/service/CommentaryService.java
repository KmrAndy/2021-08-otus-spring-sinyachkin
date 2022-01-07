package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.models.Commentary;

import java.util.List;

public interface CommentaryService {
    Long getBookCommentariesCount(long bookId) throws DataAccessException;

    Commentary addNewCommentary(long bookId, String commentaryText) throws DataAccessException;

    void changeCommentaryTextById(long id, String newText) throws DataAccessException;

    Commentary getCommentaryById(long id) throws DataAccessException;

    String getCommentaryTextById(long id) throws DataAccessException;

    List<Commentary> getCommentariesByBookId(long bookId) throws DataAccessException;

    List<String> getCommentariesTextByBookId(long bookId) throws DataAccessException;

    void deleteByCommentaryId(long id) throws DataAccessException;
}
