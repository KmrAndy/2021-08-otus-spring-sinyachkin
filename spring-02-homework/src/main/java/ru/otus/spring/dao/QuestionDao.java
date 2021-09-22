package ru.otus.spring.dao;

import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.QuestionReaderException;

import java.util.List;

public interface QuestionDao {
    List<Question> getQuestions() throws QuestionReaderException;
}
