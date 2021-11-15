package ru.otus.spring.dao;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.domain.Genre;

public interface GenreDao {
    Genre getGenreByName(String genreName) throws DataAccessException;
}
