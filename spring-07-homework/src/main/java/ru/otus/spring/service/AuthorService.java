package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.domain.Author;

public interface AuthorService {
    Author getAuthorByName(String firstName, String lastName) throws DataAccessException;
}
