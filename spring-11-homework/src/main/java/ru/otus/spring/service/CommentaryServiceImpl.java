package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.NoCommentaryFoundException;
import ru.otus.spring.exception.OtherAccessException;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.repositories.CommentaryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentaryServiceImpl implements CommentaryService{
    private final CommentaryRepository repository;
    private final BookService bookService;

    public CommentaryServiceImpl(BookService bookService, CommentaryRepository repository){
        this.repository = repository;
        this.bookService = bookService;
    }

    @Transactional(readOnly = true)
    public Long getBookCommentariesCount(Long bookId) throws DataAccessException {
        try {
            return repository.countByBook(bookService.getBookById(bookId));
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    @Transactional(readOnly = false)
    public Commentary addNewCommentary(Long bookId, String commentaryText)
            throws DataAccessException {
        try {
            return repository.save(
                    new Commentary(bookService.getBookById(bookId), commentaryText));
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    @Transactional(readOnly = false)
    public void changeCommentaryTextById(Long id, String newText) throws DataAccessException{
        int result;
        try {
            result = repository.updateCommentaryById(id, newText);
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }

        if (result == 0){
            throw new NoCommentaryFoundException("There is no commentary with id '" + id + "'");
        }
    }

    @Transactional(readOnly = true)
    public Commentary getCommentaryById(Long id) throws DataAccessException{
        Optional<Commentary> commentary;
        try {
            commentary = repository.findById(id);
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }

        if (commentary.isEmpty()) {
            throw new NoCommentaryFoundException("There is no commentary with id '" + id + "'");
        }

        return commentary.get();
    }

    @Transactional(readOnly = true)
    public String getCommentaryTextById(Long id) throws DataAccessException{
        return getCommentaryById(id).getText();
    }

    @Transactional(readOnly = true)
    public List<Commentary> getCommentariesByBookId(Long bookId) throws DataAccessException{
        try {
            return repository.findAllByBook(bookService.getBookById(bookId));
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<String> getCommentariesTextByBookId(Long bookId) throws DataAccessException{
        List<String> commentaries = new ArrayList<>();
        for(Commentary commentary : getCommentariesByBookId(bookId)){
            commentaries.add(commentary.getText());
        }
        return commentaries;
    }

    @Transactional(readOnly = false)
    public void deleteByCommentaryId(Long id) throws DataAccessException{
        try {
            repository.deleteById(id);
        } catch (DataAccessException e){
            if (e.contains(EmptyResultDataAccessException.class)){
                throw new NoCommentaryFoundException("There is no commentary with id '" + id + "'");
            } else {
                throw new OtherAccessException(e);
            }
        }
    }
}