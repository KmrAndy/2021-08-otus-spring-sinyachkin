package ru.otus.spring.service;

public interface IOStreamService {
    void printLine(String line);

    void printLine();

    String inputLine();

    void inputNext();

    boolean inputHasInt();

    int inputInt();
}
