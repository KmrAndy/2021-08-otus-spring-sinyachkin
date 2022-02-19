package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.models.AuthorJPA;
import ru.otus.spring.models.BookJPA;
import ru.otus.spring.models.GenreJPA;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Репозиторий для работы с книгами")
@DataJpaTest
class BookRepositoryJpaTest {
    private static final Long EXPECTED_BOOK_COUNT = 3L;

    @Autowired
    private BookRepository repository;

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
        AuthorJPA expectedAuthor = em.find(AuthorJPA.class, 1L);
        GenreJPA expectedGenre = em.find(GenreJPA.class, 1L);
        BookJPA expectedBook = new BookJPA(4L, "The Hobbit", expectedAuthor, expectedGenre);

        BookJPA actualBook = em.find(BookJPA.class, expectedBook.getId());
        assertThat(actualBook).isNull();

        actualBook = repository.save(expectedBook);

        assertThat(actualBook).isNotNull().usingRecursiveComparison().isEqualTo(expectedBook);
        assertThat(actualBook.getAuthor()).usingRecursiveComparison().isEqualTo(expectedAuthor);
        assertThat(actualBook.getGenre()).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Изменяем название книги по ее id")
    @Test
    void shouldUpdateBookNameById() {
        BookJPA expectedBook = em.find(BookJPA.class, 1L);
        String expectedBookName = "Test BookJPA";
        em.detach(expectedBook);

        repository.updateBookNameById(expectedBook.getId(), expectedBookName);

        BookJPA actualBook = em.find(BookJPA.class, 1L);
        assertThat(actualBook).isNotNull();
        assertThat(actualBook.getName()).isEqualTo(expectedBookName);
    }

    @DisplayName("Получаем книгу по ее id")
    @Test
    void shouldReturnBookById() {
        BookJPA expectedBook = em.find(BookJPA.class, 1L);
        Optional<BookJPA> actualBook = repository.findById(expectedBook.getId());

        assertThat(actualBook.get()).isNotNull().usingRecursiveComparison().isEqualTo(expectedBook);
        assertThat(actualBook.get().getAuthor()).usingRecursiveComparison().isEqualTo(expectedBook.getAuthor());
        assertThat(actualBook.get().getGenre()).usingRecursiveComparison().isEqualTo(expectedBook.getGenre());
    }

    @DisplayName("Получаем все книги")
    @Test
    void shouldReturnAllBooks() {
        BookJPA expectedFirstBook = em.find(BookJPA.class, 1L);
        BookJPA expectedSecondBook = em.find(BookJPA.class, 2L);
        BookJPA expectedThirdBook = em.find(BookJPA.class, 3L);

        List<BookJPA> actualBookList = repository.findAll();
        assertThat(actualBookList).isNotNull()
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedFirstBook, expectedSecondBook, expectedThirdBook);
    }

    @DisplayName("Удаляем книгу из библиотеки по ее id")
    @Test
    void shouldDeleteByBookId() {
        BookJPA expectedBook = em.find(BookJPA.class, 1L);
        assertThat(expectedBook).isNotNull();

        em.detach(expectedBook);
        repository.deleteById(expectedBook.getId());

        expectedBook = em.find(BookJPA.class, 1L);
        assertThat(expectedBook).isNull();
    }
}