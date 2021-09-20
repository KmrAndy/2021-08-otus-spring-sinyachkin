package ru.otus.spring.domain;

public class Question {

    private final String questionText;
    private final String[] answers;

    public Question(String questionText, String[] answers) {
        this.questionText = questionText;
        this.answers = answers;
    }

    public String getQuestionText() {return questionText;}

    public String[] getQuestionAnswers() {return answers;}
}
