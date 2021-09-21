package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Person;

@Service
public class PersonServiceImpl implements PersonService {
    private final IOStreamService ioStreamService;

    public PersonServiceImpl(IOStreamService ioStreamService){
        this.ioStreamService = ioStreamService;
    }

    public Person createPerson(){
        ioStreamService.printLine("Enter your First name: ");
        String firstName = ioStreamService.inputLine();
        ioStreamService.printLine("Enter your Last name: ");
        String lastName = ioStreamService.inputLine();
        ioStreamService.printLine();

        return new Person(firstName, lastName);
    }
}
