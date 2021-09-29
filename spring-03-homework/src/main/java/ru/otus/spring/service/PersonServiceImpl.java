package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Person;

@Service
public class PersonServiceImpl implements PersonService {
    private final IOService ioService;
    private final MessageService messageService;

    public PersonServiceImpl(IOService ioService, MessageService messageService){
        this.ioService = ioService;
        this.messageService = messageService;
    }

    public Person createPerson(){
        ioService.printLine(messageService.getMessage("strings.input-first-name"));
        String firstName = ioService.inputLine();

        ioService.printLine(messageService.getMessage("strings.input-last-name"));
        String lastName = ioService.inputLine();

        if ((firstName.isEmpty()) || (lastName.isEmpty())) {
            throw new IllegalArgumentException(messageService.getMessage("strings.empty-name"));
        }

        return new Person(firstName, lastName);
    }
}
