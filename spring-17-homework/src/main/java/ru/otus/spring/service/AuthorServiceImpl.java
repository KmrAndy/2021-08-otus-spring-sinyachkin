package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.NoAuthorFoundException;
import ru.otus.spring.exception.OtherAccessException;
import ru.otus.spring.models.Author;
import ru.otus.spring.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository repository;

    public AuthorServiceImpl(AuthorRepository repository){
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Author getAuthorByName(String firstName, String lastName) throws DataAccessException {
        Optional<Author> author;
        try {
            author = repository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName);
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }

        if (author.isEmpty()) {
            throw new NoAuthorFoundException(
                    "There is no author with full name '" + firstName + " " + lastName + "'");
        }

        return author.get();
    }

    @Transactional(readOnly = true)
    public List<Author> getAllAuthors() throws DataAccessException{
        try {
            return repository.findAll();
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }
}
