package ru.otus.spring.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Genre;
import ru.otus.spring.service.BookService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@AutoConfigureDataMongo
@DisplayName("Тестирование рест контроллера книг")
class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    private final List<Book> expectedBooks =
            List.of(new Book("1",
                            "FirstBook",
                            List.of(new Author("John", "Tolkien")),
                            List.of(new Genre("fantasy"))),
                    new Book("2",
                            "SecondBook",
                            List.of(new Author("Leo", "Tolstoy")),
                            List.of(new Genre("novel"))));

    @DisplayName("Получаем все книги")
    @Test
    void shouldReturnAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(expectedBooks);

        mvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(expectedBooks.get(0).getName()))
                .andExpect(jsonPath("$[1].name").value(expectedBooks.get(1).getName()))
                .andExpect(jsonPath("$[0].authors[0].firstName").value(expectedBooks.get(0).getAuthors().get(0).getFirstName()))
                .andExpect(jsonPath("$[0].authors[0].lastName").value(expectedBooks.get(0).getAuthors().get(0).getLastName()))
                .andExpect(jsonPath("$[1].authors[0].firstName").value(expectedBooks.get(1).getAuthors().get(0).getFirstName()))
                .andExpect(jsonPath("$[1].authors[0].lastName").value(expectedBooks.get(1).getAuthors().get(0).getLastName()))
                .andExpect(jsonPath("$[0].genres[0].name").value(expectedBooks.get(0).getGenres().get(0).getName()))
                .andExpect(jsonPath("$[1].genres[0].name").value(expectedBooks.get(1).getGenres().get(0).getName()));
    }

    @DisplayName("Получаем книгу")
    @Test
    void shouldReturnExpectedBook() throws Exception {
        when(bookService.getBookById("1")).thenReturn(expectedBooks.get(0));

        mvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedBooks.get(0).getName()))
                .andExpect(jsonPath("$.authors[0].firstName").value(expectedBooks.get(0).getAuthors().get(0).getFirstName()))
                .andExpect(jsonPath("$.authors[0].lastName").value(expectedBooks.get(0).getAuthors().get(0).getLastName()))
                .andExpect(jsonPath("$.genres[0].name").value(expectedBooks.get(0).getGenres().get(0).getName()));
    }

    @DisplayName("Меняем имя книги")
    @Test
    void shouldChangeBookName() throws Exception {
        String expectedBookName = "Test Book";
        Book expectedBook = expectedBooks.get(0);
        expectedBook.setName(expectedBookName);

        ArgumentCaptor<String> nameCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(bookService).changeBookNameByBookId(any(String.class), nameCapture.capture());
        when(bookService.getBookById(expectedBook.getId())).thenReturn(expectedBook);

        mvc.perform(put("/api/books/" + expectedBook.getId() + "/" + expectedBookName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedBook.getName()))
                .andExpect(jsonPath("$.authors[0].firstName").value(expectedBook.getAuthors().get(0).getFirstName()))
                .andExpect(jsonPath("$.authors[0].lastName").value(expectedBook.getAuthors().get(0).getLastName()))
                .andExpect(jsonPath("$.genres[0].name").value(expectedBook.getGenres().get(0).getName()));

        assertEquals(expectedBookName, nameCapture.getValue());
    }

    @DisplayName("Удаляем книгу")
    @Test
    void shouldDeleteBook() throws Exception {
        Book expectedBook = expectedBooks.get(0);

        ArgumentCaptor<String> idCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(bookService).deleteByBookId(idCapture.capture());

        mvc.perform(delete("/api/books/" + expectedBook.getId()))
                .andExpect(status().isOk());

        assertEquals(expectedBook.getId(), idCapture.getValue());
    }

    @DisplayName("Добавляем книгу")
    @Test
    void shouldAddBook() throws Exception {
        Book expectedBook = expectedBooks.get(0);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestBody=ow.writeValueAsString(expectedBook);

        ArgumentCaptor<String> nameCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List<Author>> authorsCapture = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<Genre>> genresCapture = ArgumentCaptor.forClass(List.class);
        when(bookService.addNewBook(nameCapture.capture(), authorsCapture.capture(), genresCapture.capture())).thenReturn(expectedBook);

        mvc.perform(post("/api/books").contentType(APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());

        assertEquals(expectedBook.getName(), nameCapture.getValue());
        assertEquals(expectedBook.getAuthors(), authorsCapture.getValue());
        assertEquals(expectedBook.getGenres(), genresCapture.getValue());
    }
}