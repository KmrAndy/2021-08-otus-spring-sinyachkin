package ru.otus.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.models.Genre;
import ru.otus.spring.service.*;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@AutoConfigureDataMongo
@DisplayName("Тестирование контроллера книг")
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentaryService commentaryService;

    @MockBean
    private UserDetailsServiceCustom userDetailsService;

    private final List<Author> expectedAuthors =
            List.of(new Author("1","John", "Tolkien"),
                    new Author("2","Leo", "Tolstoy"));

    private final List<Genre> expectedGenres =
            List.of(new Genre("1","fantasy"),
                    new Genre("2","novel"));

    private final List<Book> expectedBooks =
            List.of(new Book("1",
                            "FirstBook",
                            List.of(expectedAuthors.get(0)),
                            List.of(expectedGenres.get(0))),
                    new Book("2",
                            "SecondBook",
                            List.of(expectedAuthors.get(1)),
                            List.of(expectedGenres.get(1))));

    private final List<Commentary> expectedCommentaries =
            List.of(new Commentary("1",
                            expectedBooks.get(0),
                            "comm text"),
                    new Commentary("2",
                            expectedBooks.get(0),
                            "comm text 2"));

    @WithMockUser(value = "admin", authorities = {"ROLE_ADMIN"})
    @DisplayName("Получаем все книги")
    @Test
    void shouldReturnAllBooks() throws Exception{
        when(bookService.getAllBooks()).thenReturn(expectedBooks);

        mvc.perform(get("/booklist"))
                .andExpect(status().isOk())
                .andExpect(view().name("booklist"))
                .andExpect(model().attribute("books", expectedBooks));
    }

    @WithMockUser(value = "admin", authorities = {"ROLE_ADMIN"})
    @DisplayName("Получаем книгу")
    @Test
    void shouldReturnExpectedBook() throws Exception {
        when(bookService.getBookById("1")).thenReturn(expectedBooks.get(0));
        when(commentaryService.getCommentariesByBookId("1")).thenReturn(expectedCommentaries);

        mvc.perform(get("/bookedit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookedit"))
                .andExpect(model().attribute("book", expectedBooks.get(0)))
                .andExpect(model().attribute("commentaries", expectedCommentaries));
    }

    @WithMockUser(value = "admin", authorities = {"ROLE_ADMIN"})
    @DisplayName("Меняем имя книги")
    @Test
    void shouldChangeBookName() throws Exception {
        String expectedBookName = "Test Book";
        Book expectedBook = expectedBooks.get(0);
        expectedBook.setName(expectedBookName);

        ArgumentCaptor<String> nameCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(bookService).changeBookNameByBookId(Mockito.any(String.class), nameCapture.capture());

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestBody = ow.writeValueAsString(expectedBook);

        mvc.perform(post("/bookedit")
                        .queryParam("id", expectedBook.getId())
                        .param("name", expectedBook.getName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/booklist"));

        assertEquals(expectedBookName, nameCapture.getValue());
    }

    @WithMockUser(value = "admin", authorities = {"ROLE_ADMIN"})
    @DisplayName("Удаляем книгу")
    @Test
    void shouldDeleteBook() throws Exception {
        Book expectedBook = expectedBooks.get(0);

        ArgumentCaptor<String> idCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(bookService).deleteByBookId(idCapture.capture());

        mvc.perform(post("/bookdel")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("bookId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/booklist"));

        assertEquals(expectedBook.getId(), idCapture.getValue());
    }

    @WithMockUser(value = "admin", authorities = {"ROLE_ADMIN"})
    @DisplayName("Получаем авторов и жанры для добавления книги")
    @Test
    void shouldGetAuthorsAndGenres() throws Exception {
        when(authorService.getAllAuthors()).thenReturn(expectedAuthors);
        when(genreService.getAllGenres()).thenReturn(expectedGenres);

        mvc.perform(get("/bookadd"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookadd"))
                .andExpect(model().attribute("authors", expectedAuthors))
                .andExpect(model().attribute("genres", expectedGenres));
    }

    @WithMockUser(value = "admin", authorities = {"ROLE_ADMIN"})
    @DisplayName("Добавляем книгу")
    @Test
    void shouldAddBook() throws Exception {
        Book expectedBook = expectedBooks.get(0);

        ArgumentCaptor<String> nameCapture = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List<Author>> authorsCapture = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<Genre>> genresCapture = ArgumentCaptor.forClass(List.class);
        when(bookService.addNewBook(nameCapture.capture(), authorsCapture.capture(), genresCapture.capture())).thenReturn(expectedBook);
        when(authorService.getAuthorById(any(String.class))).thenReturn(expectedBook.getAuthors().get(0));
        when(genreService.getGenreById(any(String.class))).thenReturn(expectedBook.getGenres().get(0));

        mvc.perform(post("/bookadd")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", expectedBook.getName())
                        .param("authorsId", expectedBook.getAuthors().get(0).getId())
                        .param("genresId", expectedBook.getGenres().get(0).getId())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/booklist"));

        assertEquals(expectedBook.getName(), nameCapture.getValue());
        assertEquals(expectedBook.getAuthors(), authorsCapture.getValue());
        assertEquals(expectedBook.getGenres(), genresCapture.getValue());
    }

    @DisplayName("Редирект на авторизацию при вызове любого метода контроллера, если еще не авторизован")
    @ParameterizedTest
    @MethodSource("getRedirectArguments")
    void shouldRedirectOnLogin(String urlTemplate, String methodType) throws Exception {
        if (methodType.equals("get")){
            mvc.perform(get(urlTemplate))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("http://localhost/login"));
        } else if (methodType.equals("post")){
            mvc.perform(post(urlTemplate))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("http://localhost/login"));
        }
    }

    @WithMockUser(value = "user", authorities = {"ROLE_USER"})
    @DisplayName("Доступ запрещен при вызове метода контроллера, если роль не Admin")
    @ParameterizedTest
    @MethodSource("getAccessDeniedArguments")
    void shouldRaiseAccessDeniedException(String urlTemplate, String methodType) throws Exception {
        if (methodType.equals("get")){
            mvc.perform(get(urlTemplate))
                    .andExpect(status().is4xxClientError())
                    .andExpect(forwardedUrl("/accessdenied"));
        } else if (methodType.equals("post")){
            mvc.perform(post(urlTemplate))
                    .andExpect(status().is4xxClientError())
                    .andExpect(forwardedUrl("/accessdenied"));
        }
    }

    static Stream<Arguments> getRedirectArguments() {
        return Stream.of(
                arguments("/booklist", "get"),
                arguments("/bookedit", "get"),
                arguments("/bookedit", "post"),
                arguments("/bookdel", "post"),
                arguments("/bookadd", "get"),
                arguments("/bookadd", "post")
        );
    }

    static Stream<Arguments> getAccessDeniedArguments() {
        return Stream.of(
                arguments("/bookedit", "post"),
                arguments("/bookdel", "post"),
                arguments("/bookadd", "get"),
                arguments("/bookadd?name=1&authorsId=1&genresId=1", "post")
        );
    }
}