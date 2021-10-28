package ru.otus.spring.service;

public interface LocalizedIOService extends IOService{
    void printMessage(String messageCode, String ... args);

    void printMessage(String messageCode);
}
