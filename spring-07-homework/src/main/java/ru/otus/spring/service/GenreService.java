package ru.otus.spring.service;

import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.LibraryAccessException;

public interface GenreService {
    Genre getGenreByName(String genreName) throws LibraryAccessException;
}
