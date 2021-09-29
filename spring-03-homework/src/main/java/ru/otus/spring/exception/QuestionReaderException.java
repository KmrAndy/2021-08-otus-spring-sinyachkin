package ru.otus.spring.exception;

public class QuestionReaderException extends Exception{
    public QuestionReaderException(String message, Throwable cause){
        super(message, cause);
    }
}
