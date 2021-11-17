package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.NoBookFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dao для работы с книгами")
@JdbcTest
@Import(BookDaoJdbc.class)
class BookDaoJdbcTest {
    private static final int EXPECTED_BOOK_COUNT = 3;

    @Autowired
    private BookDaoJdbc dao;

    @DisplayName("Получаем общее количество книг")
    @Test
    void shouldReturnExpectedBookCount() {
        int actualBookCount = dao.count();
        assertThat(actualBookCount).isEqualTo(EXPECTED_BOOK_COUNT);
    }

    @DisplayName("Добавляем новую книгу")
    @Test
    void shouldInsertBook() {
        Author expectedAuthor = new Author(1L, "John", "Tolkien");
        Genre expectedGenre = new Genre(1L, "fantasy");
        Book expectedBook = new Book(4L, "The Hobbit", expectedAuthor, expectedGenre);
        long actualBookId = dao.insertBook(expectedBook);

        Book actualBook = dao.getBookById(actualBookId);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
        assertThat(actualBook.getAuthor()).usingRecursiveComparison().isEqualTo(expectedAuthor);
        assertThat(actualBook.getGenre()).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Изменяем название книги по ее id")
    @Test
    void shouldUpdateBookNameById() {
        long expectedBookId = 1;
        String expectedBookName = "Test Book";
        dao.updateBookNameById(expectedBookId, expectedBookName);

        Book actualBook = dao.getBookById(expectedBookId);
        assertThat(actualBook.getName()).isEqualTo(expectedBookName);
    }

    @DisplayName("Получаем книгу по ее id")
    @Test
    void shouldReturnBookById() {
        Author expectedAuthor = new Author(1L, "John", "Tolkien");
        Genre expectedGenre = new Genre(1L, "fantasy");
        Book expectedBook = new Book(1L, "The Lord of the Rings", expectedAuthor, expectedGenre);

        Book actualBook = dao.getBookById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
        assertThat(actualBook.getAuthor()).usingRecursiveComparison().isEqualTo(expectedAuthor);
        assertThat(actualBook.getGenre()).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Получаем все книги")
    @Test
    void shouldReturnAllBooks() {
        Author expectedFirstAuthor = new Author(1L, "John", "Tolkien");
        Author expectedSecondAuthor = new Author(2L, "Leo", "Tolstoy");
        Genre expectedFirstGenre = new Genre(1L, "fantasy");
        Genre expectedSecondGenre = new Genre(2L, "novel");
        Book expectedFirstBook = new Book(1L, "The Lord of the Rings", expectedFirstAuthor, expectedFirstGenre);
        Book expectedSecondBook = new Book(2L, "War and Piece", expectedSecondAuthor, expectedSecondGenre);
        Book expectedThirdBook = new Book(3L, "Anna Karenina", expectedSecondAuthor, expectedSecondGenre);

        List<Book> actualBookList = dao.getAllBooks();
        assertThat(actualBookList)
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedFirstBook, expectedSecondBook, expectedThirdBook);
    }

    @DisplayName("Удаляем книгу из библиотеки по ее id")
    @Test
    void shouldDeleteByBookId() {
        long bookId = 1L;
        assertThatCode(() -> dao.getBookById(bookId))
                .doesNotThrowAnyException();

        dao.deleteByBookId(bookId);

        assertThatThrownBy(() -> dao.getBookById(bookId))
                .isInstanceOf(NoBookFoundException.class);
    }
}