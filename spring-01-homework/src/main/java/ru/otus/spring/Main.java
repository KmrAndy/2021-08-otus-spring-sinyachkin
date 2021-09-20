package ru.otus.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.domain.Question;
import ru.otus.spring.service.QuestionService;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

        QuestionService service = context.getBean(QuestionService.class);
        ArrayList<Question> questions = service.getQuestions();

        System.out.println("Test Questions!");
        for(int i = 0; i < questions.size(); i++){
            System.out.println("Question "+ (i + 1) + ": " + questions.get(i).getQuestionText().trim());
            System.out.println("Answers:");

            for(String answer : questions.get(i).getQuestionAnswers()){System.out.println(answer.trim());}
            System.out.println();
        }

        context.close();
    }
}
