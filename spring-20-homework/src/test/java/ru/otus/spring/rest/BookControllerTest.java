package ru.otus.spring.rest;

import com.mongodb.client.result.UpdateResult;
import org.bson.BsonValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentaryRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@WebFluxTest(BookController.class)
@AutoConfigureDataMongo
@DisplayName("Тестирование рест контроллера книг")
class BookControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @SpyBean
    private BookRepository bookRepository;

    @SpyBean
    private CommentaryRepository commentaryRepository;

    private final Book firstBook = new Book(
            "1",
            "FirstBook",
            List.of(new Author("1","John", "Tolkien")),
            List.of(new Genre("1","fantasy")));

    private final Book secondBook = new Book(
            "2",
            "SecondBook",
            List.of(new Author("2","Leo", "Tolstoy")),
            List.of(new Genre("2","novel")));

    @DisplayName("Получаем все книги")
    @Test
    void shouldReturnAllBooks() throws Exception {
        Flux<Book> expectedBooks = Flux.just(firstBook, secondBook);
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        webTestClient.get()
                .uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Book.class)
                .value(books -> {
                    assertThat(books.get(0)).isEqualTo(firstBook);
                    assertThat(books.get(1)).isEqualTo(secondBook);
                });
    }

    @DisplayName("Получаем книгу")
    @Test
    void shouldReturnExpectedBook() throws Exception {
        Mono<Book> expectedBook = Mono.just(firstBook);
        when(bookRepository.findById("1")).thenReturn(expectedBook);

        webTestClient.get()
                .uri("/api/books/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .value(book -> {
                    assertThat(book).isEqualTo(firstBook);
                });
    }

    @DisplayName("Меняем имя книги")
    @Test
    void shouldChangeBookName() throws Exception {
        String expectedBookName = "Test Book";
        Book book = firstBook;
        book.setName(expectedBookName);

        Mono<Book> expectedBook = Mono.just(book);

        when(bookRepository.save(book)).thenReturn(expectedBook);
        when(commentaryRepository.updateCommentariesBookByBook(book))
                .thenReturn(Mono.just(UpdateResult.acknowledged(1L, 1L, null)));

        webTestClient.patch()
                .uri("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(expectedBook, Book.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .value(result -> {
                    assertThat(result).isEqualTo(book);
                });
    }

    @DisplayName("Удаляем книгу")
    @Test
    void shouldDeleteBook() throws Exception {
        when(bookRepository.deleteById("1")).thenReturn(Mono.empty());
        when(commentaryRepository.deleteCommentariesByBookId("1")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/books/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @DisplayName("Добавляем книгу")
    @Test
    void shouldAddBook() throws Exception {
        when(bookRepository.save(firstBook)).thenReturn(Mono.just(firstBook));

        webTestClient.post()
                .uri("/api/books")
                .body(Mono.just(firstBook), Book.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .value(resultBook -> {
                    assertThat(resultBook).isEqualTo(firstBook);
                });


    }
}