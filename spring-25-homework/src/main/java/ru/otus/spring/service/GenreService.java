package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.models.Genre;

import java.util.List;

public interface GenreService {
    Genre getGenreByName(String genreName) throws DataAccessException;

    List<Genre> getAllGenres() throws DataAccessException;

    Genre getGenreById(String id) throws DataAccessException;
}
