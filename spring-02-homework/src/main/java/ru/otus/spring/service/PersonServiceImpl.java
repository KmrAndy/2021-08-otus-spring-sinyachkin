package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Person;

@Service
public class PersonServiceImpl implements PersonService {
    private final IOService ioService;

    public PersonServiceImpl(IOService ioService){
        this.ioService = ioService;
    }

    public Person createPerson(){
        ioService.printLine("Enter your First name: ");
        String firstName = ioService.inputLine();
        ioService.printLine("Enter your Last name: ");
        String lastName = ioService.inputLine();
        ioService.printEmptyLine();

        return new Person(firstName, lastName);
    }
}
