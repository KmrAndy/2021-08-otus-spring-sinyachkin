package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.models.Commentary;

import java.util.List;

public interface CommentaryService {
    Long getBookCommentariesCount(Long bookId) throws DataAccessException;

    Commentary addNewCommentary(Long bookId, String commentaryText) throws DataAccessException;

    void changeCommentaryTextById(Long id, String newText) throws DataAccessException;

    Commentary getCommentaryById(Long id) throws DataAccessException;

    String getCommentaryTextById(Long id) throws DataAccessException;

    List<Commentary> getCommentariesByBookId(Long bookId) throws DataAccessException;

    List<String> getCommentariesTextByBookId(Long bookId) throws DataAccessException;

    void deleteByCommentaryId(Long id) throws DataAccessException;
}
