package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.Question;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {
    private final QuestionService questionService;
    private final PersonService personService;
    private final IOService ioService;

    public QuizServiceImpl(QuestionService questionService, PersonService personService, IOService ioService) {
        this.questionService = questionService;
        this.personService = personService;
        this.ioService = ioService;
    }

    public void run(){
        int result;
        List<Question> questions;

        Person player = personService.createPerson();
        try {
            questions = questionService.getQuestions();
        }
        catch(Exception e){
            ioService.printLine(e.getMessage());
            return;
        }

        //Starting quiz
        result = startQuiz(questions);

        //Printing result
        ioService.printLine("Testing Result");
        ioService.printLine(player.getName() + ": " + result + "/" + questions.size());
    }

    private int startQuiz(List<Question> questions){
        int result = 0;
        ioService.printLine("Testing Questions!");

        for(int i = 0; i < questions.size(); i++) {
            ioService.printLine("Question "+ (i + 1) + ": " + questions.get(i).getQuestionText().trim());
            result = answerQuestion(questions.get(i)) + result;

            // newline
            ioService.printEmptyLine();
        }

        return result;
    }

    private int answerQuestion(Question question){
        int userAnswer;
        ioService.printLine("Answers:");

        List<String> answers = question.getQuestionAnswers();
        for(int i = 0; i < answers.size(); i++) {
            ioService.printLine((i + 1) + ". " + answers.get(i));
        }

        userAnswer = inputValue(answers.size());

        if (answers.get(userAnswer - 1).equals(question.getQuestionRightAnswer())){
            return 1;
        }
        else {
            return 0;
        }
    }

    private int inputValue(int answersCount) {
        int userAnswer = 0;

        boolean isValueCorrect = false;
        while (!isValueCorrect) {
            ioService.printLine("Choose right answer:");

            try {
                userAnswer = ioService.inputInt();
            } catch (IllegalArgumentException e) {
                ioService.printLine(e.getMessage());
                ioService.inputNext();
                continue;
            }

            if ((userAnswer < 1) || (userAnswer > answersCount)) {
                ioService.printLine("Answer value must be integer from 1 to " + answersCount);
                continue;
            }

            isValueCorrect = true;
        }

        return userAnswer;
    }
}

