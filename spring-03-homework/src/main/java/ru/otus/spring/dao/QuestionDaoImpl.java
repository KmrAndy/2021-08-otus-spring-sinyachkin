package ru.otus.spring.dao;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.QuestionReaderException;
import ru.otus.spring.service.FilenameService;
import ru.otus.spring.service.MessageService;

@Repository
public class QuestionDaoImpl implements QuestionDao {

    private final String questionFileName;
    private final MessageService messageService;

    public QuestionDaoImpl(FilenameService filenameService, MessageService messageService){
        this.questionFileName = filenameService.getFilename();
        this.messageService = messageService;
    }

    private Question parseQuestionLine(String line){
        List<String> chunks = List.of(line.split(";"));
        // Question without answers or null line
        if (chunks.size() < 2){
            throw new IllegalArgumentException(messageService.getMessage("strings.incorrect-file-line", line));
        }

        //First column is question, last - right answer
        return new Question(chunks.get(0), chunks.subList(1, chunks.size() - 1), chunks.get(chunks.size() - 1));
    }

    private List<Question> parseFileAndCollectQuestions(InputStream inputStream) throws IOException {
        List<Question> questions = new ArrayList<>();

        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);

        String line;
        while ((line = reader.readLine()) != null) {
            questions.add(parseQuestionLine(line));
        }

        return questions;
    }

    public List<Question> getQuestions() throws QuestionReaderException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(this.questionFileName);

        // get and parse file data
        try {
            if (inputStream == null) {
                throw new IllegalArgumentException(messageService.getMessage("strings.file-not-found",
                        this.questionFileName));
            } else {
                return parseFileAndCollectQuestions(inputStream);
            }
        } catch (Exception e) {
            throw new QuestionReaderException(messageService.getMessage("strings.error-read-file"), e);
        }
    }
}