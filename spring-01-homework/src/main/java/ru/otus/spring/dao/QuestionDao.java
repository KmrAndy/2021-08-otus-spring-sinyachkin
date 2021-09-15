package ru.otus.spring.dao;

import ru.otus.spring.domain.Question;

import java.util.ArrayList;

public interface QuestionDao {
    ArrayList<Question> getQuestions();
}
