package ru.otus.spring.exception;

import org.springframework.dao.DataAccessException;

public class NoAuthorFoundException extends DataAccessException {
    public NoAuthorFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
