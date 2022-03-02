package ru.otus.spring.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoWaiterFoundException extends DataAccessException {
    public NoWaiterFoundException(String message, Throwable cause){
        super(message, cause);
    }

    public NoWaiterFoundException(String message){
        super(message);
    }
}
