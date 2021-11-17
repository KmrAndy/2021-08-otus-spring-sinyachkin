package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.NoBookFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Сервис для работы с книгами")
@SpringBootTest
class BookServiceImplTest {

    @MockBean
    private BookDao bookDao;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("Получаем общее количество книг")
    @Test
    void shouldReturnExpectedBookCount() {
        int expectedBooksCount = 3;

        when(bookDao.count()).thenReturn(expectedBooksCount);
        assertEquals(bookDao.count(), bookService.getBooksCount());
    }

    @DisplayName("Добавляем новую книгу")
    @Test
    void shouldAddNewBook() {
        Author expectedAuthor = new Author(1L, "John", "Tolkien");
        Genre expectedGenre = new Genre(1L, "fantasy");
        Book insertedBook = new Book("The Hobbit", expectedAuthor, expectedGenre);
        Book expectedBook = new Book(4L, "The Hobbit", expectedAuthor, expectedGenre);

        when(bookDao.insertBook(insertedBook))
                .thenReturn(expectedBook.getId());
        when(authorService.getAuthorByName(expectedAuthor.getFirstName(), expectedAuthor.getLastName()))
                .thenReturn(expectedAuthor);
        when(genreService.getGenreByName(expectedGenre.getName())).thenReturn(expectedGenre);

        long bookId = bookService.addNewBook(insertedBook.getName(), insertedBook.getAuthor().getFirstName(),
                insertedBook.getAuthor().getLastName(), insertedBook.getGenre().getName());

        when(bookDao.getBookById(bookId))
                .thenReturn(expectedBook);

        Book actualBook = bookService.getBookById(bookId);

        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Изменяем название книги по ее id")
    @Test
    void shouldChangeBookNameByBookId() {
        long bookId = 1;
        String expectedBookName = "Test Book";
        ArgumentCaptor<String> bookNameCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(bookDao).updateBookNameById(any(Long.class), bookNameCapture.capture());

        bookService.changeBookNameByBookId(bookId,expectedBookName);
        assertEquals(expectedBookName, bookNameCapture.getValue());
    }

    @DisplayName("Получаем книгу по ее id")
    @Test
    void shouldReturnBookById() {
        Author expectedAuthor = new Author(1L, "John", "Tolkien");
        Genre expectedGenre = new Genre(1L, "fantasy");
        Book expectedBook = new Book(1L, "The Lord of the Rings", expectedAuthor, expectedGenre);

        when(bookDao.getBookById(expectedBook.getId())).thenReturn(expectedBook);
        assertEquals(bookService.getBookById(expectedBook.getId()), expectedBook);
    }

    @DisplayName("Получаем информацию строкой о книге по ее id")
    @Test
    void getBookInfoById() {
        Author expectedAuthor = new Author(1L, "John", "Tolkien");
        Genre expectedGenre = new Genre(1L, "fantasy");
        Book expectedBook = new Book(1L, "The Lord of the Rings", expectedAuthor, expectedGenre);

        when(bookDao.getBookById(expectedBook.getId())).thenReturn(expectedBook);
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

        when(bookDao.getAllBooks()).thenReturn(expectedBooks);
        assertEquals(bookService.getAllBooks(), expectedBooks);
    }

    @DisplayName("Удаляем книгу из библиотеки по ее id")
    @Test
    void deleteByBookId() {
        long expectedBookId = 1;
        ArgumentCaptor<Long> bookIdCapture = ArgumentCaptor.forClass(Long.class);
        doNothing().when(bookDao).deleteByBookId(bookIdCapture.capture());

        bookService.deleteByBookId(expectedBookId);
        assertEquals(expectedBookId, bookIdCapture.getValue());

        doThrow(NoBookFoundException.class).when(bookDao).deleteByBookId(isA(Long.class));
        assertThatThrownBy(() -> bookService.deleteByBookId(expectedBookId))
                .isInstanceOf(NoBookFoundException.class);
    }
}