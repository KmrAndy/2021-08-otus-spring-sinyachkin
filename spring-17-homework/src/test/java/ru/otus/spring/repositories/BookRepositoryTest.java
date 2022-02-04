package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataMongoTest
@DisplayName("Репозиторий для работы с книгами")
class BookRepositoryTest {
    private static final long EXPECTED_BOOK_COUNT = 3L;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @DisplayName("Получаем общее количество книг")
    @Test
    void shouldReturnExpectedBookCount() {
        long actualBookCount = bookRepository.count();
        assertThat(actualBookCount).isEqualTo(EXPECTED_BOOK_COUNT);
    }

    @DisplayName("Добавляем новую книгу")
    @Test
    void shouldInsertBook() {
        Author expectedAuthor = authorRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase("John", "Tolkien").get();
        Genre expectedGenre = genreRepository.getGenreByNameIgnoreCase("fantasy").get();
        Book expectedBook = new Book("The Hobbit", List.of(expectedAuthor), List.of(expectedGenre));

        Book actualBook = bookRepository.save(expectedBook);

        assertThat(actualBook.getName()).isEqualTo(expectedBook.getName());
        assertThat(actualBook.getAuthors()).usingRecursiveComparison().isEqualTo(expectedBook.getAuthors());
        assertThat(actualBook.getGenres()).usingRecursiveComparison().isEqualTo(expectedBook.getGenres());

        bookRepository.delete(expectedBook);
    }

    @DisplayName("Изменяем название книги по ее id")
    @Test
    void shouldUpdateBookNameById() {
        Book expectedBook = bookRepository.findAll().get(0);
        String expectedBookName = "Test Book";
        expectedBook.setName(expectedBookName);

        Book actualBook = bookRepository.save(expectedBook);

        assertThat(actualBook.getName()).isEqualTo(expectedBookName);
    }

    @DisplayName("Получаем книгу по ее id")
    @Test
    void shouldReturnBookById() {
        Book expectedBook = bookRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).get(0);
        Optional<Book> actualBook = bookRepository.findById(expectedBook.getId());

        assertThat(actualBook.get()).isNotNull().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Удаляем книгу из библиотеки по ее id")
    @Test
    void shouldDeleteByBookId() {
        Book expectedBook = bookRepository.findAll().get(0);
        assertThat(expectedBook).isNotNull();

        bookRepository.deleteById(expectedBook.getId());
        Optional<Book> actualBook = bookRepository.findById(expectedBook.getId());

        assertThat(actualBook).isEmpty();

        expectedBook.setId(null);
        bookRepository.save(expectedBook);
    }
}