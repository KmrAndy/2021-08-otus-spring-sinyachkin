package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.models.Genre;

public interface GenreService {
    Genre getGenreByName(String genreName) throws DataAccessException;
}
