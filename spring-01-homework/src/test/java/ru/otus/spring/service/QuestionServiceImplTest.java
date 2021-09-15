package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Тест класса QuestionServiceImpl")
@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    private QuestionDao dao;

    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        questionService = new QuestionServiceImpl(dao);
    }

    @DisplayName("Тест метода getQuestions")
    @Test
    void shouldReturnSameQuestionsAsDao() {
        ArrayList<Question> questionsList = new ArrayList<Question>();
        questionsList.add(new Question("QuestionText", new String[] {"Answer1", "Answer2"}));
        questionsList.add(new Question("TextQuestion", new String[] {"Answer3", "Answer4", "Answer5"}));

        when(dao.getQuestions()).thenReturn(questionsList);
        assertEquals(dao.getQuestions(), questionService.getQuestions());
    }
}