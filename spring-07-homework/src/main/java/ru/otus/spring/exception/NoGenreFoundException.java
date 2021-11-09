package ru.otus.spring.exception;

public class NoGenreFoundException extends LibraryAccessException {
    public NoGenreFoundException(String message, Throwable cause){
        super(message, cause);
    }
}