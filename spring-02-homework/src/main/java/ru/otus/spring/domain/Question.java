package ru.otus.spring.domain;

import java.util.List;

public class Question {

    private final String questionText;
    private final List<String> answers;
    private final String rightAnswer;

    public Question(String questionText, List<String> answers, String rightAnswer) {
        this.questionText = questionText;
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }

    public String getQuestionText() {return questionText;}

    public List<String> getQuestionAnswers() {return answers;}

    public String getQuestionRightAnswer() {return rightAnswer;}
}
