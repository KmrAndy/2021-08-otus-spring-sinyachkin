package ru.otus.spring.domain;

public class Question {

    private final String questionText;
    private final String[] answers;
    private final String rightAnswer;

    public Question(String questionText, String[] answers, String rightAnswer) {
        this.questionText = questionText;
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }

    public String getQuestionText() {return questionText;}

    public String[] getQuestionAnswers() {return answers;}

    public String getQuestionRightAnswer() {return rightAnswer;}
}
