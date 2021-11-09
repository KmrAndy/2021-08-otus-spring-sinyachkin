package ru.otus.spring.exception;

public class UniqueBookIdException extends LibraryAccessException {
    public UniqueBookIdException(String message, Throwable cause){
        super(message, cause);
    }
}