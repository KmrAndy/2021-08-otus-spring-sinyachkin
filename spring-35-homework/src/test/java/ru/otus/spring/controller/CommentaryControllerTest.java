package ru.otus.spring.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.models.Genre;
import ru.otus.spring.service.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(CommentaryController.class)
@AutoConfigureDataMongo
@DisplayName("Тестирование рест контроллера комментариев")
class CommentaryControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentaryService commentaryService;

    private final Book expectedBook = new Book(
            "1",
            "FirstBook",
            List.of(new Author("1","John", "Tolkien")),
            List.of(new Genre("1","fantasy")));

    private final Commentary expectedCommentary = new Commentary(
            "1",
            expectedBook,
            "comm text");


    @DisplayName("Получаем комментарий")
    @Test
    void shouldReturnExpectedCommentary() throws Exception {
        when(commentaryService.getCommentaryById(expectedCommentary.getId())).thenReturn(expectedCommentary);

        mvc.perform(get("/commedit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .queryParam("id", expectedCommentary.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("commedit"))
                .andExpect(model().attribute("commentary", expectedCommentary));
    }

    @DisplayName("Меняем текст комментария")
    @Test
    void shouldChangeCommentaryText() throws Exception {
        String expectedCommentaryText = "Test Commentary";
        Commentary expectedCommentary = this.expectedCommentary;
        expectedCommentary.setText(expectedCommentaryText);

        ArgumentCaptor<String> textCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(commentaryService).changeCommentaryTextById(any(String.class), textCapture.capture());
        when(commentaryService.getCommentaryById(expectedCommentary.getId())).thenReturn(expectedCommentary);

        mvc.perform(post("/commedit")
                        .queryParam("id", expectedCommentary.getId())
                        .param("text", expectedCommentary.getText()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bookedit?id=" + expectedCommentary.getBook().getId()));

        assertEquals(expectedCommentaryText, textCapture.getValue());
    }

    @DisplayName("Удаляем комментарий")
    @Test
    void shouldDeleteCommentary() throws Exception {
        ArgumentCaptor<String> idCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(commentaryService).deleteByCommentaryId(idCapture.capture());

        mvc.perform(post("/commdel")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("commentaryId", expectedCommentary.getId())
                        .param("bookId", expectedCommentary.getBook().getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bookedit?id=" + expectedCommentary.getBook().getId()));

        assertEquals(expectedCommentary.getId(), idCapture.getValue());
    }

    @DisplayName("Получаем книгу для добавления комментария")
    @Test
    void shouldGetBook() throws Exception {
        when(bookService.getBookById(expectedBook.getId())).thenReturn(expectedBook);

        mvc.perform(get("/commadd")
                        .param("bookId", expectedBook.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("commadd"))
                .andExpect(model().attribute("book", expectedBook));
    }

    @DisplayName("Добавляем комментарий")
    @Test
    void shouldAddCommentary() throws Exception {
        ArgumentCaptor<String> textCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Book> bookCapture = ArgumentCaptor.forClass(Book.class);
        when(commentaryService.addNewCommentary(bookCapture.capture(), textCapture.capture())).thenReturn(expectedCommentary);
        when(bookService.getBookById(expectedCommentary.getBook().getId())).thenReturn(expectedCommentary.getBook());

        mvc.perform(post("/commadd")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("bookId", expectedBook.getId())
                        .param("text", expectedCommentary.getText())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bookedit?id=" + expectedBook.getId()));

        assertEquals(expectedCommentary.getBook(), bookCapture.getValue());
        assertEquals(expectedCommentary.getText(), textCapture.getValue());
    }

}