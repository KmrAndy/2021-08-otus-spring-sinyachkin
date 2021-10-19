package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@Service
public class LocalizedIOServiceImpl implements LocalizedIOService{
    private final PrintStream printStream;
    private final Scanner scanner;
    private final MessageService messageService;

    public LocalizedIOServiceImpl(@Value("#{T(java.lang.System).in}") InputStream inputStream,
                                  @Value("#{T(java.lang.System).out}") PrintStream printStream,
                                  MessageService messageService){
        this.printStream = printStream;
        scanner = new Scanner(inputStream);
        this.messageService = messageService;
    }

    public void printMessage(String messageCode, String ... args){
        printLine(messageService.getMessage(messageCode, args));
    }

    public void printMessage(String messageCode){
        printLine(messageService.getMessage(messageCode));
    }

    @Override
    public void printLine(String line) {
        printStream.println(line);
    }

    @Override
    public void printEmptyLine() {
        printStream.println();
    }

    @Override
    public String inputLine() {
        return scanner.nextLine().trim();
    }

    @Override
    public void inputNext(){ scanner.next(); }

    @Override
    public int inputInt(){
        if (scanner.hasNextInt()){
            return scanner.nextInt();
        }
        else{
            throw new IllegalArgumentException(messageService.getMessage("strings.int-value-exception"));
        }
    }
}
