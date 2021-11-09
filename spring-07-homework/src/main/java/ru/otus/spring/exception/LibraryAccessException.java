package ru.otus.spring.exception;

import org.springframework.core.NestedRuntimeException;

public abstract class LibraryAccessException extends NestedRuntimeException {
    public LibraryAccessException(String message, Throwable cause){
        super(message, cause);
    }

    public LibraryAccessException(String message){
        super(message);
    }
}
