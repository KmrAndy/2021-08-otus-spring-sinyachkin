package ru.otus.spring.service;

import ru.otus.spring.domain.Author;
import ru.otus.spring.exception.LibraryAccessException;

public interface AuthorService {
    Author getAuthorByName(String firstName, String lastName) throws LibraryAccessException;
}
