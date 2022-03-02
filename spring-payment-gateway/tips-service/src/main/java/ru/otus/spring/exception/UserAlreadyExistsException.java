package ru.otus.spring.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends DataAccessException {
    public UserAlreadyExistsException(String message, Throwable cause){
        super(message, cause);
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
