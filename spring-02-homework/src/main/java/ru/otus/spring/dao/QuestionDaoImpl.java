package ru.otus.spring.dao;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Question;

@Repository
public class QuestionDaoImpl implements QuestionDao {

    private final String questionFileName;

    public QuestionDaoImpl(@Value("${question.filename}") String questionFileName){
        this.questionFileName = questionFileName;
    }

    private Question parseQuestionLine(String line){
        String[] chunks = line.split(";");
        // Question without answers or null line
        if (chunks.length < 2){
            throw new IllegalArgumentException("incorrect file line '" + line + "'");
        }

        //First column is question, last - right answer
        return new Question(chunks[0], Arrays.copyOfRange(chunks, 1, chunks.length - 1), chunks[chunks.length - 1]);
    }

    private ArrayList<Question> parseFileAndCollectQuestions(InputStream inputStream){
        ArrayList<Question> questions = new ArrayList<Question>();

        try (InputStreamReader streamReader =
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                questions.add(parseQuestionLine(line));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public ArrayList<Question> getQuestions(){
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(this.questionFileName);

        // get and parse file data
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + this.questionFileName);
        } else {
            return parseFileAndCollectQuestions(inputStream);
        }
    }
}