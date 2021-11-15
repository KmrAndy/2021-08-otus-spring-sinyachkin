package ru.otus.spring.exception;

import org.springframework.dao.DataAccessException;

public class OtherAccessException extends DataAccessException {

    private static final String EXCEPTION_TEXT = "There is an error while interacting with DAO";
    public OtherAccessException(Throwable cause){
        super(EXCEPTION_TEXT, cause);
    }
}
