package ru.otus.spring.service;

import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.QuestionReaderException;

import java.io.IOException;
import java.util.List;

public interface QuestionService {
    List<Question> getQuestions() throws QuestionReaderException;
}
