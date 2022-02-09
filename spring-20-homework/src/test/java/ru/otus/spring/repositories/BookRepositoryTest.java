package ru.otus.spring.repositories;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataMongoTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Репозиторий для работы с книгами")
class BookRepositoryTest {
    private static final Long EXPECTED_BOOK_COUNT =3L;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    private Author expectedAuthor;

    private Genre expectedGenre;

    private Book expectedBook;

    @BeforeEach
    private void init(){
        expectedAuthor = authorRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .collectList()
                .block()
                .get(0);

        expectedGenre = genreRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .collectList()
                .block()
                .get(0);

        expectedBook = bookRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .collectList()
                .block()
                .get(0);
    }

    @DisplayName("Получаем общее количество книг")
    @Order(1)
    @Test
    void shouldReturnExpectedBookCount() {
        Mono<Long> actualBookCount = bookRepository.count();
        StepVerifier
                .create(actualBookCount)
                .assertNext(count -> assertThat(count).isEqualTo(EXPECTED_BOOK_COUNT))
                .expectComplete()
                .verify();
    }

    @DisplayName("Добавляем новую книгу")
    @Order(2)
    @Test
    void shouldInsertBook() {
        Book expectedBook = new Book("The Hobbit", List.of(expectedAuthor), List.of(expectedGenre));

        Mono<Book> actualBook = bookRepository.save(expectedBook);

        StepVerifier
                .create(actualBook)
                .assertNext(book -> {
                    assertThat(book).isNotNull();
                    assertThat(book.getName()).isEqualTo(expectedBook.getName());
                    assertThat(book.getAuthors()).usingRecursiveComparison().isEqualTo(expectedBook.getAuthors());
                    assertThat(book.getGenres()).usingRecursiveComparison().isEqualTo(expectedBook.getGenres());
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("Изменяем название книги по ее id")
    @Order(3)
    @Test
    void shouldUpdateBookNameById() {
        String expectedBookName = "Test Book";
        expectedBook.setName(expectedBookName);

        Mono<Book> actualBook = bookRepository.save(expectedBook);

        StepVerifier
                .create(actualBook)
                .assertNext(book -> assertThat(book.getName()).isEqualTo(expectedBookName))
                .expectComplete()
                .verify();

    }

    @DisplayName("Получаем книгу по ее id")
    @Order(4)
    @Test
    void shouldReturnBookById() {
        Mono<Book> actualBook = bookRepository.findById(expectedBook.getId());

        StepVerifier
                .create(actualBook)
                .assertNext(book -> assertThat(book).isNotNull().usingRecursiveComparison().isEqualTo(expectedBook))
                .expectComplete()
                .verify();
    }

    @DisplayName("Удаляем книгу из библиотеки по ее id")
    @Order(5)
    @Test
    void shouldDeleteByBookId() {
        Book expectedBook = bookRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .collectList()
                .block()
                .get(0);
        assertThat(expectedBook).isNotNull();

        Mono<Void> deleteResult = bookRepository.deleteById(expectedBook.getId());

        StepVerifier
                .create(deleteResult)
                .expectNextCount(0)
                .verifyComplete();

        Mono<Book> actualBook = bookRepository.findById(expectedBook.getId());

        StepVerifier
                .create(actualBook)
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }
}