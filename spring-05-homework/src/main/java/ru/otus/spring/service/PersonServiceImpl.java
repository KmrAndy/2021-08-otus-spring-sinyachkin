package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Person;

@Service
public class PersonServiceImpl implements PersonService {
    private final LocalizedIOService localizedIOService;

    public PersonServiceImpl(LocalizedIOService localizedIOService){
        this.localizedIOService = localizedIOService;
    }

    public Person createPerson(){
        String firstName = null;
        String lastName = null;

        boolean isNameCorrect = false;
        while (!isNameCorrect) {
            localizedIOService.printMessage("strings.input-first-name");
            firstName = localizedIOService.inputLine();

            localizedIOService.printMessage("strings.input-last-name");
            lastName = localizedIOService.inputLine();

            if ((firstName.isEmpty()) || (lastName.isEmpty())) {
                localizedIOService.printMessage("strings.empty-name");
                continue;
            }
            isNameCorrect = true;
        }

        return new Person(firstName, lastName);
    }
}
