package ru.otus.spring.shell;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.spring.domain.Person;
import ru.otus.spring.service.MessageService;
import ru.otus.spring.service.PersonService;
import ru.otus.spring.service.QuizService;

@ShellComponent
public class ApplicationCommands {
    private final QuizService quizService;
    private final PersonService personService;
    private final MessageService messageService;
    private Person player;

    public ApplicationCommands(QuizService quizService, PersonService personService,
                               MessageService messageService){
        this.quizService = quizService;
        this.personService = personService;
        this.messageService = messageService;
    }

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login() {
        this.player = personService.createPerson();
        return messageService.getMessage("strings.greetings", player.getName());
    }

    @ShellMethod(value = "Run quiz command", key = {"r", "run"})
    @ShellMethodAvailability(value = "isPersonNamed")
    public String runQuiz() {
        quizService.run(this.player);
        return messageService.getMessage("strings.quiz-over");
    }

    private Availability isPersonNamed() {
        return this.player == null? Availability.unavailable(messageService.getMessage("strings.need-login")): Availability.available();
    }
}
