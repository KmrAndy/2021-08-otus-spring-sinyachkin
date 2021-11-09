package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.dao.LibraryDao;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.LibraryAccessException;

@Service
public class GenreServiceImpl implements GenreService{
    private final LibraryDao dao;

    public GenreServiceImpl(LibraryDao dao){ this.dao = dao; }

    public Genre getGenreByName(String genreName) throws LibraryAccessException {
        return dao.getGenreByName(genreName);
    }
}
