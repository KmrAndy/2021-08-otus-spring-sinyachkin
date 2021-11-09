package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.dao.LibraryDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.NoBookFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Dao для работы с библиотекой")
@SpringBootTest
class BookServiceImplTest {

    @MockBean
    private LibraryDao dao;

    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("Получаем общее количество книг")
    @Test
    void shouldReturnExpectedBookCount() {
        int expectedBooksCount = 3;

        when(dao.count()).thenReturn(expectedBooksCount);
        assertEquals(dao.count(), bookService.getBooksCount());
    }

    @DisplayName("Добавляем новую книгу")
    @Test
    void shouldAddNewBook() {
        Author expectedAuthor = new Author(1L, "John", "Tolkien");
        Genre expectedGenre = new Genre(1L, "fantasy");
        Book expectedBook = new Book(4L, "The Hobbit", expectedAuthor, expectedGenre);

        ArgumentCaptor<Book> bookCapture = ArgumentCaptor.forClass(Book.class);
        doNothing().when(dao).insertBook(bookCapture.capture());
        when(dao.getAuthorByFullName(expectedAuthor.getFirstName(), expectedAuthor.getLastName()))
                .thenReturn(expectedAuthor);
        when(dao.getGenreByName(expectedGenre.getName())).thenReturn(expectedGenre);

        bookService.addNewBook(expectedBook.getId(), expectedBook.getName(), expectedBook.getAuthor().getFirstName(),
                expectedBook.getAuthor().getLastName(), expectedBook.getGenre().getName());

        assertEquals(expectedBook, bookCapture.getValue());
    }

    @DisplayName("Изменяем название книги по ее id")
    @Test
    void shouldChangeBookNameByBookId() {
        long expectedBookId = 1;
        String expectedBookName = "Test Book";
        ArgumentCaptor<String> bookNameCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(dao).updateBookNameById(any(Long.class), bookNameCapture.capture());

        bookService.changeBookNameByBookId(expectedBookId,expectedBookName);
        assertEquals(expectedBookName, bookNameCapture.getValue());
    }

    @DisplayName("Получаем книгу по ее id")
    @Test
    void shouldReturnBookById() {
        Author expectedAuthor = new Author(1L, "John", "Tolkien");
        Genre expectedGenre = new Genre(1L, "fantasy");
        Book expectedBook = new Book(1L, "The Lord of the Rings", expectedAuthor, expectedGenre);

        when(dao.getBookById(expectedBook.getId())).thenReturn(expectedBook);
        assertEquals(bookService.getBookById(expectedBook.getId()), expectedBook);
    }

    @DisplayName("Получаем информацию строкой о книге по ее id")
    @Test
    void getBookInfoById() {
        Author expectedAuthor = new Author(1L, "John", "Tolkien");
        Genre expectedGenre = new Genre(1L, "fantasy");
        Book expectedBook = new Book(1L, "The Lord of the Rings", expectedAuthor, expectedGenre);

        when(dao.getBookById(expectedBook.getId())).thenReturn(expectedBook);
        assertEquals(bookService.getBookInfoById(expectedBook.getId()), String.valueOf(expectedBook));
    }

    @DisplayName("Получаем все книги")
    @Test
    void getAllBooks() {
        List<Book> expectedBooks = new ArrayList<>();
        Author expectedFirstAuthor = new Author(1L, "John", "Tolkien");
        Author expectedSecondAuthor = new Author(2L, "Leo", "Tolstoy");
        Genre expectedFirstGenre = new Genre(1L, "fantasy");
        Genre expectedSecondGenre = new Genre(2L, "novel");
        expectedBooks.add(new Book(1L, "The Lord of the Rings", expectedFirstAuthor, expectedFirstGenre));
        expectedBooks.add(new Book(2L, "War and Piece", expectedSecondAuthor, expectedSecondGenre));
        expectedBooks.add(new Book(3L, "Anna Karenina", expectedSecondAuthor, expectedSecondGenre));

        when(dao.getAllBooks()).thenReturn(expectedBooks);
        assertEquals(bookService.getAllBooks(), expectedBooks);
    }

    @DisplayName("Удаляем книгу из библиотеки по ее id")
    @Test
    void deleteByBookId() {
        long expectedBookId = 1;
        ArgumentCaptor<Long> bookIdCapture = ArgumentCaptor.forClass(Long.class);
        doNothing().when(dao).deleteByBookId(bookIdCapture.capture());

        bookService.deleteByBookId(expectedBookId);
        assertEquals(expectedBookId, bookIdCapture.getValue());

        doThrow(NoBookFoundException.class).when(dao).deleteByBookId(isA(Long.class));
        assertThatThrownBy(() -> bookService.deleteByBookId(expectedBookId))
                .isInstanceOf(NoBookFoundException.class);
    }
}