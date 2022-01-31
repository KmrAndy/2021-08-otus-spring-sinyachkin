package ru.otus.spring.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.models.Author;
import ru.otus.spring.service.AuthorService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Тестирование рест контроллера авторов")
class AuthorControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    @DisplayName("Получаем всех авторов")
    @Test
    void shouldReturnAllAuthors() throws Exception {
        List<Author> expectedAuthors = new ArrayList<>();
        expectedAuthors.add(new Author("1", "John", "Tolkien"));
        expectedAuthors.add(new Author("2", "Leo", "Tolstoy"));

        when(authorService.getAllAuthors()).thenReturn(expectedAuthors);

        mvc.perform(get("/api/authorlist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value(expectedAuthors.get(0).getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(expectedAuthors.get(0).getLastName()))
                .andExpect(jsonPath("$[1].firstName").value(expectedAuthors.get(1).getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(expectedAuthors.get(1).getLastName()));
    }
}