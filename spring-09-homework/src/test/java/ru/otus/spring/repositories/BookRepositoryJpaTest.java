package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Репозиторий для работы с книгами")
@DataJpaTest
@Import(BookRepositoryJpa.class)
class BookRepositoryJpaTest {
    private static final Long EXPECTED_BOOK_COUNT = 3L;

    @Autowired
    private BookRepositoryJpa repository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Получаем общее количество книг")
    @Test
    void shouldReturnExpectedBookCount() {
        Long actualBookCount = repository.count();
        assertThat(actualBookCount).isEqualTo(EXPECTED_BOOK_COUNT);
    }

    @DisplayName("Добавляем новую книгу")
    @Test
    void shouldInsertBook() {
        Author expectedAuthor = em.find(Author.class, 1L);
        Genre expectedGenre = em.find(Genre.class, 1L);
        Book expectedBook = new Book(4L, "The Hobbit", expectedAuthor, expectedGenre);

        Book actualBook = em.find(Book.class, expectedBook.getId());
        assertThat(actualBook).isNull();

        long actualBookId = repository.insertBook(expectedBook);
        actualBook = em.find(Book.class, actualBookId);

        assertThat(actualBook).isNotNull().usingRecursiveComparison().isEqualTo(expectedBook);
        assertThat(actualBook.getAuthor()).usingRecursiveComparison().isEqualTo(expectedAuthor);
        assertThat(actualBook.getGenre()).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Изменяем название книги по ее id")
    @Test
    void shouldUpdateBookNameById() {
        Book expectedBook = em.find(Book.class, 1L);
        String expectedBookName = "Test Book";
        em.detach(expectedBook);

        repository.updateBookNameById(expectedBook.getId(), expectedBookName);

        Book actualBook = em.find(Book.class, 1L);
        assertThat(actualBook).isNotNull();
        assertThat(actualBook.getName()).isEqualTo(expectedBookName);
    }

    @DisplayName("Получаем книгу по ее id")
    @Test
    void shouldReturnBookById() {
        Book expectedBook = em.find(Book.class, 1L);
        Book actualBook = repository.getBookById(expectedBook.getId());

        assertThat(actualBook).isNotNull().usingRecursiveComparison().isEqualTo(expectedBook);
        assertThat(actualBook.getAuthor()).usingRecursiveComparison().isEqualTo(expectedBook.getAuthor());
        assertThat(actualBook.getGenre()).usingRecursiveComparison().isEqualTo(expectedBook.getGenre());
    }

    @DisplayName("Получаем все книги")
    @Test
    void shouldReturnAllBooks() {
        Book expectedFirstBook = em.find(Book.class, 1L);
        Book expectedSecondBook = em.find(Book.class, 2L);
        Book expectedThirdBook = em.find(Book.class, 3L);

        List<Book> actualBookList = repository.getAllBooks();
        assertThat(actualBookList).isNotNull()
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedFirstBook, expectedSecondBook, expectedThirdBook);
    }

    @DisplayName("Удаляем книгу из библиотеки по ее id")
    @Test
    void shouldDeleteByBookId() {
        Book expectedBook = em.find(Book.class, 1L);
        assertThat(expectedBook).isNotNull();

        em.detach(expectedBook);
        repository.deleteByBookId(expectedBook.getId());

        expectedBook = em.find(Book.class, 1L);
        assertThat(expectedBook).isNull();
    }
}