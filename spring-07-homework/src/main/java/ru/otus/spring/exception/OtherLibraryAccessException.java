package ru.otus.spring.exception;

public class OtherLibraryAccessException extends LibraryAccessException{

    private static final String EXCEPTION_TEXT = "There is an error while interacting with DAO";
    public OtherLibraryAccessException(Throwable cause){
        super(EXCEPTION_TEXT, cause);
    }
}
