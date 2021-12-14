package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.models.Author;
import ru.otus.spring.repositories.AuthorRepository;

@Service
public class AuthorServiceImpl implements AuthorService{
    private final AuthorRepository repository;

    public AuthorServiceImpl(AuthorRepository repository){
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Author getAuthorByName(String firstName, String lastName) throws DataAccessException {
        return repository.getAuthorByFullName(firstName, lastName);
    }
}
