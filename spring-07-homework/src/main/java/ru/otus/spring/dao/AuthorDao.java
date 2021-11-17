package ru.otus.spring.dao;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.domain.Author;

public interface AuthorDao {
    Author getAuthorByFullName(String firstName, String lastName) throws DataAccessException;
}
