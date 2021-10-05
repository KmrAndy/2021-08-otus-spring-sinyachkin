package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Person;

@Service
public class PersonServiceImpl implements PersonService {
    private final IOService ioService;
    private final PrintMessageService printMsgService;

    public PersonServiceImpl(IOService ioService, PrintMessageService printMsgService){
        this.ioService = ioService;
        this.printMsgService = printMsgService;
    }

    public Person createPerson(){
        String firstName = null;
        String lastName = null;

        boolean isNameCorrect = false;
        while (!isNameCorrect) {
            printMsgService.printMessage("strings.input-first-name");
            firstName = ioService.inputLine();

            printMsgService.printMessage("strings.input-last-name");
            lastName = ioService.inputLine();

            if ((firstName.isEmpty()) || (lastName.isEmpty())) {
                printMsgService.printMessage("strings.empty-name");
                continue;
            }
            isNameCorrect = true;
        }

        return new Person(firstName, lastName);
    }
}
