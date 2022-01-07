package ru.otus.spring.repositories;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.models.Author;

public interface AuthorRepository {
    Author getAuthorByFullName(String firstName, String lastName) throws DataAccessException;
}
