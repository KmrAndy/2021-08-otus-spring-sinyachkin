package ru.otus.spring.service;

public interface PrintMessageService {
    void printMessage(String messageCode, String ... args);

    void printMessage(String messageCode);
}
