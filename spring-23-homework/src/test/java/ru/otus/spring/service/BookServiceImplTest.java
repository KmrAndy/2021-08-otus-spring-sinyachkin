package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.models.Genre;
import ru.otus.spring.exception.NoBookFoundException;
import ru.otus.spring.repositories.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("Сервис для работы с книгами")
class BookServiceImplTest {

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentaryService commentaryService;

    @Autowired
    private BookServiceImpl bookService;

    private Author expectedAuthor = new Author("1", "John", "Tolkien");
    private Genre expectedGenre = new Genre("1", "fantasy");
    private Book expectedBook = new Book("4", "The Hobbit", List.of(expectedAuthor), List.of(expectedGenre));

    @DisplayName("Получаем общее количество книг")
    @Test
    void shouldReturnExpectedBookCount() {
        Long expectedBooksCount = 3L;

        when(bookRepository.count()).thenReturn(expectedBooksCount);
        assertEquals(bookRepository.count(), bookService.getBooksCount());
    }

    @DisplayName("Добавляем новую книгу")
    @Test
    void shouldAddNewBook() {
        Book insertedBook = new Book("The Hobbit", List.of(expectedAuthor), List.of(expectedGenre));

        when(bookRepository.save(insertedBook))
                .thenReturn(expectedBook);
        when(authorService.getAuthorByName(expectedAuthor.getFirstName(), expectedAuthor.getLastName()))
                .thenReturn(expectedAuthor);
        when(genreService.getGenreByName(expectedGenre.getName())).thenReturn(expectedGenre);

        Book actualBook = bookService.addNewBook(insertedBook.getName(), expectedAuthor.getFirstName(),
                expectedAuthor.getLastName(), expectedGenre.getName());

        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Изменяем название книги по ее id")
    @Test
    void shouldChangeBookNameByBookId() {
        String expectedBookName = "Test Book";
        List<Commentary> expectedCommentaries = new ArrayList<>();
        ArgumentCaptor<Book> bookCapture = ArgumentCaptor.forClass(Book.class);
        when(bookRepository.findById(expectedBook.getId())).thenReturn(Optional.of(expectedBook));
        when(bookRepository.save(bookCapture.capture())).thenReturn(expectedBook);
        doNothing().when(commentaryService).changeCommentariesBook(any(Book.class), eq(expectedCommentaries));

        bookService.changeBookNameByBookId(expectedBook.getId(),expectedBookName);
        assertEquals(expectedBookName, bookCapture.getValue().getName());
    }

    @DisplayName("Получаем книгу по ее id")
    @Test
    void shouldReturnBookById() {
        when(bookRepository.findById(expectedBook.getId())).thenReturn(Optional.of(expectedBook));
        assertEquals(bookService.getBookById(expectedBook.getId()), expectedBook);
    }

    @DisplayName("Получаем информацию строкой о книге по ее id")
    @Test
    void getBookInfoById() {
        when(bookRepository.findById(expectedBook.getId())).thenReturn(Optional.of(expectedBook));
        assertEquals(bookService.getBookInfoById(expectedBook.getId()), String.valueOf(expectedBook));
    }

    @DisplayName("Получаем все книги")
    @Test
    void getAllBooks() {
        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(expectedBook);

        when(bookRepository.findAll()).thenReturn(expectedBooks);
        assertEquals(bookService.getAllBooks(), expectedBooks);
    }

    @DisplayName("Удаляем книгу из библиотеки по ее id")
    @Test
    void deleteByBookId() {
        ArgumentCaptor<Book> bookCapture = ArgumentCaptor.forClass(Book.class);
        when(bookRepository.findById(expectedBook.getId())).thenReturn(Optional.of(expectedBook));
        doNothing().when(bookRepository).delete(bookCapture.capture());
        doNothing().when(commentaryService).deleteBookCommentaries(any(Book.class));

        bookService.deleteByBookId(expectedBook.getId());
        assertEquals(expectedBook, bookCapture.getValue());

        when(bookRepository.findById(expectedBook.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.deleteByBookId(expectedBook.getId()))
                .isInstanceOf(NoBookFoundException.class);
    }
}