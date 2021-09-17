package ru.otus.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.Quiz;
import ru.otus.spring.service.QuestionService;

import java.util.Scanner;

@ComponentScan
@PropertySource("classpath:application.properties")
@Configuration
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        QuestionService service = context.getBean(QuestionService.class);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your First name: ");
        String firstName = scanner.nextLine().trim();
        System.out.println("Enter your Last name: ");
        String lastName = scanner.nextLine().trim();
        System.out.println();

        Person student = new Person(firstName, lastName);
        Quiz quiz = new Quiz(student, service.getQuestions());

        //Starting quiz
        quiz.start();

        //Printing result
        System.out.println("Testing Result");
        System.out.println(quiz.getResult());

        context.close();
    }
}
