package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import ru.otus.spring.domain.Genre;

public interface GenreService {
    Genre getGenreByName(String genreName) throws DataAccessException;
}
