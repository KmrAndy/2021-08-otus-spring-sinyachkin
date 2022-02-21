package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.NoGenreFoundException;
import ru.otus.spring.exception.OtherAccessException;
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService{
    private final GenreRepository repository;

    public GenreServiceImpl(GenreRepository repository){
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Genre getGenreByName(String genreName) throws DataAccessException {
        Optional<Genre> genre;
        try {
            genre = repository.getGenreByNameIgnoreCase(genreName);
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }

        if (genre.isEmpty()) {
            throw new NoGenreFoundException("There is no genre with name '" + genreName + "'");
        }

        return genre.get();
    }

    @Transactional(readOnly = true)
    public List<Genre> getAllGenres() throws DataAccessException{
        try {
            return repository.findAll();
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    @Transactional(readOnly = true)
    public Genre getGenreById(String id) throws DataAccessException{
        Optional<Genre> genre;
        try {
            genre = repository.findById(id);
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }

        if (genre.isEmpty()) {
            throw new NoGenreFoundException("There is no genre with id '" + id + "'");
        }

        return genre.get();
    }
}
