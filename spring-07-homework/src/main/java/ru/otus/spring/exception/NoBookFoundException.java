package ru.otus.spring.exception;

public class NoBookFoundException extends LibraryAccessException {
    public NoBookFoundException(String message, Throwable cause){
        super(message, cause);
    }

    public NoBookFoundException(String message){
        super(message);
    }
}
