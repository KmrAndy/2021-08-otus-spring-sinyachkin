package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Genre;

@Service
public class GenreServiceImpl implements GenreService{
    private final GenreDao dao;

    public GenreServiceImpl(GenreDao dao){
        this.dao = dao;
    }

    public Genre getGenreByName(String genreName) throws DataAccessException {
        return dao.getGenreByName(genreName);
    }
}
