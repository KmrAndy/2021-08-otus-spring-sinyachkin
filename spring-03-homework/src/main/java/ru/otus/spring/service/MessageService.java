package ru.otus.spring.service;

public interface MessageService {
    String getMessage(String messageCode, String[] args);

    String getMessage(String messageCode);

    String getMessage(String messageCode, String arg);
}
