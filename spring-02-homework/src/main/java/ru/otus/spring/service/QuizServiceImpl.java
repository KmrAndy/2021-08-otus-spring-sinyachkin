package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Person;
import ru.otus.spring.domain.Question;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {
    private final QuestionService questionService;
    private final PersonService personService;
    private final IOStreamService ioStreamService;

    public QuizServiceImpl(QuestionService questionService, PersonService personService, IOStreamService ioStreamService) {
        this.questionService = questionService;
        this.personService = personService;
        this.ioStreamService = ioStreamService;
    }

    public void run(){
        int result = 0;

        Person player = personService.createPerson();
        List<Question> questions = questionService.getQuestions();

        //Starting quiz
        List<String> answers;
        int userAnswer = 0;
        boolean isValueCorrect;
        ioStreamService.printLine("Testing Questions!");

        for(int i = 0; i < questions.size(); i++){
            ioStreamService.printLine("Question "+ (i + 1) + ": " + questions.get(i).getQuestionText().trim());
            answers = questions.get(i).getQuestionAnswers();
            ioStreamService.printLine("Answers:");

            for(int j = 0; j < answers.size(); j++) {
                ioStreamService.printLine((j + 1) + ". " + answers.get(j));
            }

            isValueCorrect = false;
            while (!isValueCorrect) {
                ioStreamService.printLine("Choose right answer:");

                if (ioStreamService.inputHasInt()){
                    userAnswer = ioStreamService.inputInt();

                    if ((userAnswer < 1) || (userAnswer > answers.size())){
                        System.out.println("Answer value must be integer from 1 to " + answers.size());
                        continue;
                    }

                    isValueCorrect = true;
                }
                else{
                    ioStreamService.inputNext();
                    System.out.println("Answer value must be integer!");
                    continue;
                }
            }

            if (answers.get(userAnswer - 1).equals(questions.get(i).getQuestionRightAnswer())){
                result++;
            }

            // newline
            ioStreamService.printLine();
        }

        //Printing result
        ioStreamService.printLine("Testing Result");
        ioStreamService.printLine(player.getName() + ": " + result + "/" + questions.size());
    };
}
