package ru.otus.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.models.Genre;
import ru.otus.spring.service.CommentaryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Тестирование рест контроллера комментариев")
class CommentaryControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentaryService commentaryService;

    private final Book expectedBook = new Book(
            "1",
            "FirstBook",
            List.of(new Author("John", "Tolkien")),
            List.of(new Genre("fantasy")));

    private final List<Commentary> expectedCommentaries =
            List.of(new Commentary("1",expectedBook,"testcomm1"),
                    new Commentary("2",expectedBook,"testcomm2"));

    @DisplayName("Получаем все комментарии книги")
    @Test
    void shouldReturnAllBookCommentaries() throws Exception {
        when(commentaryService.getCommentariesByBookId(expectedBook.getId())).thenReturn(expectedCommentaries);

        mvc.perform(get("/api/commlist/" + expectedBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value(expectedCommentaries.get(0).getText()))
                .andExpect(jsonPath("$[1].text").value(expectedCommentaries.get(1).getText()))
                .andExpect(jsonPath("$[0].book.name").value(expectedCommentaries.get(0).getBook().getName()))
                .andExpect(jsonPath("$[1].book.name").value(expectedCommentaries.get(1).getBook().getName()));
    }

    @DisplayName("Получаем комментарий")
    @Test
    void shouldReturnExpectedCommentary() throws Exception {
        when(commentaryService.getCommentaryById(expectedCommentaries.get(0).getId())).thenReturn(expectedCommentaries.get(0));

        mvc.perform(get("/api/commedit/" + expectedCommentaries.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(expectedCommentaries.get(0).getText()))
                .andExpect(jsonPath("$.book.name").value(expectedCommentaries.get(0).getBook().getName()));
    }

    @DisplayName("Меняем текст комментария")
    @Test
    void shouldChangeCommentaryText() throws Exception {
        String expectedCommentaryText = "Test Book";
        Commentary expectedCommentary = expectedCommentaries.get(0);
        expectedCommentary.setText(expectedCommentaryText);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestBody=ow.writeValueAsString(expectedCommentary);

        ArgumentCaptor<String> textCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(commentaryService).changeCommentaryTextById(any(String.class), textCapture.capture());

        mvc.perform(post("/api/commedit").contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());

        assertEquals(expectedCommentaryText, textCapture.getValue());
    }

    @DisplayName("Удаляем комментарий")
    @Test
    void shouldDeleteCommentary() throws Exception {
        Commentary expectedCommentary = expectedCommentaries.get(0);
        String requestBody = expectedCommentary.getId();

        ArgumentCaptor<String> idCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(commentaryService).deleteByCommentaryId(idCapture.capture());

        mvc.perform(post("/api/commdel").contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());

        assertEquals(expectedCommentary.getId(), idCapture.getValue());
    }

    @DisplayName("Добавляем комментарий")
    @Test
    void shouldAddCommentary() throws Exception {
        Commentary expectedCommentary = expectedCommentaries.get(0);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestBody=ow.writeValueAsString(expectedCommentary);

        ArgumentCaptor<String> textCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Book> bookCapture = ArgumentCaptor.forClass(Book.class);
        when(commentaryService.addNewCommentary(bookCapture.capture(), textCapture.capture())).thenReturn(expectedCommentary);

        mvc.perform(post("/api/commadd").contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());

        assertEquals(expectedCommentary.getBook(), bookCapture.getValue());
        assertEquals(expectedCommentary.getText(), textCapture.getValue());
    }

}