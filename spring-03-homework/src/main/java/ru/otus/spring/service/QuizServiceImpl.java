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
    private final PrintMessageService printMsgService;

    private static final int ANSWER_IS_RIGHT = 1;
    private static final int ANSWER_IS_WRONG = 0;

    public QuizServiceImpl(QuestionService questionService, PersonService personService,
                           IOService ioService, PrintMessageService printMsgService) {
        this.questionService = questionService;
        this.personService = personService;
        this.ioService = ioService;
        this.printMsgService = printMsgService;
    }

    public void run(){
        List<Question> questions;

        Person player = personService.createPerson();

        ioService.printEmptyLine();
        printMsgService.printMessage("strings.greetings", player.getName());

        try {
            questions = questionService.getQuestions();
        }
        catch(Exception e){
            printMsgService.printMessage("strings.error-prepare-questions", e.getMessage());
            return;
        }

        //Starting quiz
        int result = startQuiz(questions);

        //Printing result
        printMsgService.printMessage("strings.quiz-result-label");
        ioService.printLine(player.getName() + ": " + result + "/" + questions.size());
    }

    private int startQuiz(List<Question> questions){
        int result = 0;
        printMsgService.printMessage("strings.quiz-label");

        for(int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            printMsgService.printMessage("strings.question-label",
                    String.valueOf(i + 1), question.getQuestionText().trim());

            printMsgService.printMessage("strings.answers-label");

            List<String> answers = question.getQuestionAnswers();
            for(int j = 0; j < answers.size(); j++) {
                ioService.printLine((j + 1) + ". " + answers.get(j));
            }

            result = answerQuestion(answers, question.getQuestionRightAnswer()) + result;

            // newline
            ioService.printEmptyLine();
        }

        return result;
    }

    private int answerQuestion(List<String> answers, String rightAnswer){
        int userAnswer = inputValue(answers.size());
        return (answers.get(userAnswer - 1).equals(rightAnswer))? ANSWER_IS_RIGHT: ANSWER_IS_WRONG;
    }

    private int inputValue(int answersCount) {
        int userAnswer = 0;

        boolean isValueCorrect = false;
        while (!isValueCorrect) {
            printMsgService.printMessage("strings.choose-answer");

            try {
                userAnswer = ioService.inputInt();
            } catch (IllegalArgumentException e) {
                ioService.printLine(e.getMessage());
                ioService.inputNext();
                continue;
            }

            if ((userAnswer < 1) || (userAnswer > answersCount)) {
                printMsgService.printMessage("strings.answer-value-exception", String.valueOf(answersCount));
                continue;
            }

            isValueCorrect = true;
        }

        return userAnswer;
    }
}

