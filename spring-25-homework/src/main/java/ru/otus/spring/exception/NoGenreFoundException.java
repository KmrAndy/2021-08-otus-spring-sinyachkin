package ru.otus.spring.exception;

import org.springframework.dao.DataAccessException;

public class NoGenreFoundException extends DataAccessException {
    public NoGenreFoundException(String message, Throwable cause){
        super(message, cause);
    }

    public NoGenreFoundException(String message){
        super(message);
    }
}