package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.dao.LibraryDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.exception.LibraryAccessException;

@Service
public class AuthorServiceImpl implements AuthorService{
    private final LibraryDao dao;

    public AuthorServiceImpl(LibraryDao dao){ this.dao = dao; }

    public Author getAuthorByName(String firstName, String lastName) throws LibraryAccessException{
        return dao.getAuthorByFullName(firstName, lastName);
    }
}
