package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.models.Author;

import java.util.List;

public interface AuthorService {
    Author getAuthorByName(String firstName, String lastName) throws DataAccessException;

    List<Author> getAllAuthors() throws DataAccessException;

    Author getAuthorById(String id) throws DataAccessException;
}
