package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.NoCommentaryFoundException;
import ru.otus.spring.exception.OtherAccessException;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.repositories.CommentaryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentaryServiceImpl implements CommentaryService{
    private final CommentaryRepository repository;

    public CommentaryServiceImpl(CommentaryRepository repository){
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Long getBookCommentariesCount(String bookId) throws DataAccessException {
        try {
            return repository.countByBook(bookId);
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    @Transactional(readOnly = false)
    public Commentary addNewCommentary(Book book, String commentaryText)
            throws DataAccessException {
        try {
            return repository.save(
                    new Commentary(book, commentaryText));
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    @Transactional(readOnly = false)
    public void changeCommentaryTextById(String id, String newText) throws DataAccessException{
        int result;
        try {
            result = repository.updateTextById(id, newText);
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }

        if (result == 0){
            throw new NoCommentaryFoundException("There is no commentary with id '" + id + "'");
        }
    }

    @Transactional(readOnly = false)
    public void changeCommentariesBook(Book newBook, List<Commentary> commentaries)  throws DataAccessException{
        try {
            repository.updateCommentariesBook(newBook, commentaries);
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    @Transactional(readOnly = true)
    public Commentary getCommentaryById(String id) throws DataAccessException{
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
    public String getCommentaryTextById(String id) throws DataAccessException{
        return getCommentaryById(id).getText();
    }

    @Transactional(readOnly = true)
    public List<Commentary> getCommentariesByBookId(String bookId) throws DataAccessException{
        try {
            return repository.findAllByBook(bookId);
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<String> getCommentariesTextByBookId(String bookId) throws DataAccessException{
        List<String> commentaries = new ArrayList<>();
        for(Commentary commentary : getCommentariesByBookId(bookId)){
            commentaries.add(String.valueOf(commentary));
        }
        return commentaries;
    }

    @Transactional(readOnly = false)
    public void deleteByCommentaryId(String id) throws DataAccessException{
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

    @Transactional(readOnly = false)
    public void deleteBookCommentaries(Book book) throws DataAccessException{
        try {
            repository.deleteCommentariesByBook(book);
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }
}