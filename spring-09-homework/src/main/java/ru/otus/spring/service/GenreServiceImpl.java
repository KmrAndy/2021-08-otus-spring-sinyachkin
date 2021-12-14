package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.GenreRepository;

@Service
public class GenreServiceImpl implements GenreService{
    private final GenreRepository repository;

    public GenreServiceImpl(GenreRepository repository){
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Genre getGenreByName(String genreName) throws DataAccessException {
        return repository.getGenreByName(genreName);
    }
}
