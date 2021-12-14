package ru.otus.spring.exception;

import org.springframework.dao.DataAccessException;

public class NoBookFoundException extends DataAccessException {
    public NoBookFoundException(String message, Throwable cause){
        super(message, cause);
    }

    public NoBookFoundException(String message){
        super(message);
    }
}
