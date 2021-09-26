package ru.otus.spring.dao;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.QuestionReaderException;

@Repository
public class QuestionDaoImpl implements QuestionDao {

    private final String questionFileName;

    public QuestionDaoImpl(@Value("${question.filename}") String questionFileName){
        this.questionFileName = questionFileName;
    }

    private Question parseQuestionLine(String line){
        List<String> chunks = List.of(line.split(";"));
        // Question without answers or null line
        if (chunks.size() < 2){
            throw new IllegalArgumentException("incorrect file line '" + line + "'");
        }

        //First column is question, last - right answer
        return new Question(chunks.get(0), chunks.subList(1, chunks.size() - 1), chunks.get(chunks.size() - 1));
    }

    private List<Question> parseFileAndCollectQuestions(InputStream inputStream) throws QuestionReaderException {
        List<Question> questions = new ArrayList<>();

        try (InputStreamReader streamReader =
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                questions.add(parseQuestionLine(line));
            }

        } catch (IOException e) {
            throw new QuestionReaderException("Error during reading questions from file", e);
        }
        return questions;
    }

    public List<Question> getQuestions() throws QuestionReaderException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(this.questionFileName);

        // get and parse file data
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + this.questionFileName);
        } else {
            return parseFileAndCollectQuestions(inputStream);
        }
    }
}