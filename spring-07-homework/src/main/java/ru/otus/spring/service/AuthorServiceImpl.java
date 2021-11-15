package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.domain.Author;

@Service
public class AuthorServiceImpl implements AuthorService{
    private final AuthorDao dao;

    public AuthorServiceImpl(AuthorDao dao){
        this.dao = dao;
    }

    public Author getAuthorByName(String firstName, String lastName) throws DataAccessException {
        return dao.getAuthorByFullName(firstName, lastName);
    }
}
