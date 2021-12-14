package ru.otus.spring.repositories;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.models.Genre;

public interface GenreRepository {
    Genre getGenreByName(String genreName) throws DataAccessException;
}
