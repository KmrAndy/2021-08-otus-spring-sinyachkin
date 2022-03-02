package ru.otus.spring.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoCardFoundException extends DataAccessException {
    public NoCardFoundException(String message, Throwable cause){
        super(message, cause);
    }

    public NoCardFoundException(String message) {
        super(message);
    }
}
