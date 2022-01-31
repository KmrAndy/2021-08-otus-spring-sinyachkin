package ru.otus.spring.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.models.Genre;
import ru.otus.spring.service.GenreService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Тестирование рест контроллера жанров")
class GenreControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @DisplayName("Получаем все жанры")
    @Test
    void shouldReturnAllGenres() throws Exception {
        List<Genre> expectedGenres = new ArrayList<>();
        expectedGenres.add(new Genre("1", "fantasy"));
        expectedGenres.add(new Genre("2", "novel"));

        when(genreService.getAllGenres()).thenReturn(expectedGenres);

        mvc.perform(get("/api/genrelist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(expectedGenres.get(0).getName()))
                .andExpect(jsonPath("$[1].name").value(expectedGenres.get(1).getName()));
    }
}