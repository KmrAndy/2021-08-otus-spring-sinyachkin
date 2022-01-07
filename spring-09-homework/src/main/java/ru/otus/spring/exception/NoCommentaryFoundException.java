package ru.otus.spring.exception;

import org.springframework.dao.DataAccessException;

public class NoCommentaryFoundException extends DataAccessException {
    public NoCommentaryFoundException(String message, Throwable cause){
        super(message, cause);
    }
    public NoCommentaryFoundException(String message){
        super(message);
    }
}
