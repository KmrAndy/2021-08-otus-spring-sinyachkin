package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@Service
public class IOStreamServiceImpl implements IOService {
    private final PrintStream printStream;
    private final Scanner scanner;

    public IOStreamServiceImpl(@Value("#{T(java.lang.System).in}") InputStream inputStream,
                               @Value("#{T(java.lang.System).out}") PrintStream printStream){
        this.printStream = printStream;
        scanner = new Scanner(inputStream);
    }

    public void printLine(String line){
        printStream.println(line);
    }

    public void printEmptyLine(){
        printStream.println();
    }

    public String inputLine(){
        return scanner.nextLine().trim();
    }

    public int inputInt(){
        if (scanner.hasNextInt()){
            return scanner.nextInt();
        }
        else{
            throw new IllegalArgumentException("Answer value must be integer!");
        }
    }

    public void inputNext(){ scanner.next(); }

}
