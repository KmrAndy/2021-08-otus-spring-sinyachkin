package ru.otus.spring.exception;

public class NoAuthorFoundException extends LibraryAccessException {
    public NoAuthorFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
