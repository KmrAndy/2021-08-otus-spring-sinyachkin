package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.Question;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {
    private final QuestionService questionService;
    private final LocalizedIOService localizedIOService;

    private static final int ANSWER_IS_RIGHT = 1;
    private static final int ANSWER_IS_WRONG = 0;

    public QuizServiceImpl(QuestionService questionService, LocalizedIOService localizedIOService) {
        this.questionService = questionService;
        this.localizedIOService = localizedIOService;
    }

    public void run(Person player){
        List<Question> questions;

        try {
            questions = questionService.getQuestions();
        }
        catch(Exception e){
            localizedIOService.printMessage("strings.error-prepare-questions", e.getMessage());
            return;
        }

        //Starting quiz
        int result = startQuiz(questions);

        //Printing result
        localizedIOService.printMessage("strings.quiz-result-label");
        localizedIOService.printLine(player.getName() + ": " + result + "/" + questions.size());
    }

    private int startQuiz(List<Question> questions){
        int result = 0;
        localizedIOService.printMessage("strings.quiz-label");

        for(int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            localizedIOService.printMessage("strings.question-label",
                    String.valueOf(i + 1), question.getQuestionText().trim());

            localizedIOService.printMessage("strings.answers-label");

            List<String> answers = question.getQuestionAnswers();
            for(int j = 0; j < answers.size(); j++) {
                localizedIOService.printLine((j + 1) + ". " + answers.get(j));
            }

            result = answerQuestion(answers, question.getQuestionRightAnswer()) + result;

            // newline
            localizedIOService.printEmptyLine();
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
            localizedIOService.printMessage("strings.choose-answer");

            try {
                userAnswer = localizedIOService.inputInt();
            } catch (IllegalArgumentException e) {
                localizedIOService.printLine(e.getMessage());
                localizedIOService.inputNext();
                continue;
            }

            if ((userAnswer < 1) || (userAnswer > answersCount)) {
                localizedIOService.printMessage("strings.answer-value-exception", String.valueOf(answersCount));
                continue;
            }

            isValueCorrect = true;
        }

        return userAnswer;
    }
}

