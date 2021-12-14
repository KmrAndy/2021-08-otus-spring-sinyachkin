package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.repositories.CommentaryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentaryServiceImpl implements CommentaryService{
    private final CommentaryRepository repository;
    private final BookService bookService;

    public CommentaryServiceImpl(BookService bookService, CommentaryRepository repository){
        this.repository = repository;
        this.bookService = bookService;
    }

    @Transactional(readOnly = true)
    public Long getBookCommentariesCount(long bookId) throws DataAccessException {
        return repository.count(bookService.getBookById(bookId));
    }

    @Transactional(readOnly = false)
    public long addNewCommentary(long bookId, String commentaryText)
            throws DataAccessException {
        return repository.insertCommentary(
                new Commentary(bookService.getBookById(bookId), commentaryText));
    }

    @Transactional(readOnly = false)
    public void changeCommentaryTextById(long id, String newText) throws DataAccessException{
        repository.updateCommentaryById(id, newText);
    }

    @Transactional(readOnly = true)
    public Commentary getCommentaryById(long id) throws DataAccessException{
        return repository.getCommentaryById(id);
    }

    @Transactional(readOnly = true)
    public String getCommentaryTextById(long id) throws DataAccessException{
        return getCommentaryById(id).getText();
    }

    @Transactional(readOnly = true)
    public List<Commentary> getCommentariesByBookId(long bookId) throws DataAccessException{
        return repository.getCommentariesByBookId(bookService.getBookById(bookId));
    }

    @Transactional(readOnly = true)
    public List<String> getCommentariesTextByBookId(long bookId) throws DataAccessException{
        List<String> commentaries = new ArrayList<>();
        for(Commentary commentary : getCommentariesByBookId(bookId)){
            commentaries.add(commentary.getText());
        }
        return commentaries;
    }

    @Transactional(readOnly = false)
    public void deleteByCommentaryId(long id) throws DataAccessException{
        repository.deleteByCommentaryId(id);
    }
}