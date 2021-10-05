package ru.otus.spring.service;

import org.springframework.stereotype.Service;

@Service
public class PrintMessageServiceImpl implements PrintMessageService{
    private final IOService ioService;
    private final MessageService messageService;

    public PrintMessageServiceImpl(MessageService messageService, IOService ioService) {
        this.ioService = ioService;
        this.messageService = messageService;
    }

    public void printMessage(String messageCode, String ... args){
        ioService.printLine(messageService.getMessage(messageCode, args));
    }

    public void printMessage(String messageCode){
        ioService.printLine(messageService.getMessage(messageCode));
    }

}
