package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.QuestionReaderException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Тест класса QuestionServiceImpl")
@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    private QuestionDao dao;

    @InjectMocks
    private QuestionServiceImpl questionService;

    @DisplayName("Тест метода getQuestions")
    @Test
    void shouldReturnSameQuestionsAsDao() throws QuestionReaderException {
        List<Question> questionsList = new ArrayList<>();
        questionsList.add(new Question("QuestionText", List.of("Answer1", "Answer2"), "Answer1"));
        questionsList.add(new Question("TextQuestion", List.of("Answer3", "Answer4", "Answer5"), "Answer5"));

        when(dao.getQuestions()).thenReturn(questionsList);
        assertEquals(dao.getQuestions(), questionService.getQuestions());
    }
}