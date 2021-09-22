package ru.otus.spring.service;

public interface IOService {
    void printLine(String line);

    void printEmptyLine();

    String inputLine();

    void inputNext();

    int inputInt();
}
