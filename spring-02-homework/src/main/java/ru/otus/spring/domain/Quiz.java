package ru.otus.spring.domain;

import java.util.ArrayList;
import java.util.Scanner;

public class Quiz {

    private final ArrayList<Question> questions;
    private final Person player;
    private int result;

    public Quiz(Person player, ArrayList<Question> questions) {
        this.questions = questions;
        this.player = player;
        this.result = 0;
    }

    public void start(){
        Scanner scanner = new Scanner(System.in);
        String[] answers;
        int userAnswer = 0;
        boolean isValueCorrect;
        System.out.println("Testing Questions!");

        for(int i = 0; i < questions.size(); i++){
            System.out.println("Question "+ (i + 1) + ": " + questions.get(i).getQuestionText().trim());
            answers = questions.get(i).getQuestionAnswers();
            System.out.println("Answers:");

            for(int j = 0; j < answers.length; j++) {
                System.out.println((j + 1) + ". " + answers[j]);
            }

            isValueCorrect = false;
            while (!isValueCorrect) {
                System.out.println("Choose right answer:");

                if (scanner.hasNextInt()){
                    userAnswer = scanner.nextInt();

                    if ((userAnswer < 1) || (userAnswer > answers.length)){
                        System.out.println("Answer value must be integer from 1 to " + answers.length);
                        continue;
                    }

                    isValueCorrect = true;
                }
                else{
                    scanner.next();
                    System.out.println("Answer value must be integer!");
                    continue;
                }
            }

            if (answers[userAnswer - 1].equals(questions.get(i).getQuestionRightAnswer())){
                result++;
            }

            // newline
            System.out.println();
        }
    }

    public String getResult(){ return player.getName() + ": " + result + "/" + questions.size(); }

}