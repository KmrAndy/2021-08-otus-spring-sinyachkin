package ru.otus.spring.rest;

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
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.CommentaryRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebFluxTest(CommentaryController.class)
@AutoConfigureDataMongo
@DisplayName("Тестирование рест контроллера комментариев")
class CommentaryControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @SpyBean
    private CommentaryRepository commentaryRepository;

    private final Book expectedBook = new Book(
            "1",
            "FirstBook",
            List.of(new Author("John", "Tolkien")),
            List.of(new Genre("fantasy")));

    private final Commentary firstCommentary = new Commentary("1",expectedBook,"testcomm1");
    private final Commentary secondCommentary = new Commentary("2",expectedBook,"testcomm2");

    @DisplayName("Получаем все комментарии книги")
    @Test
    void shouldReturnAllBookCommentaries() throws Exception {
        Flux<Commentary> expectedCommentaries = Flux.just(firstCommentary, secondCommentary);
        when(commentaryRepository.findAllByBook(expectedBook.getId())).thenReturn(expectedCommentaries);

        webTestClient.get()
                .uri("/api/comments/book/" + expectedBook.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Commentary.class)
                .value(commentaries -> {
                    assertThat(commentaries.get(0)).isEqualTo(firstCommentary);
                    assertThat(commentaries.get(1)).isEqualTo(secondCommentary);
                });
    }

    @DisplayName("Получаем комментарий")
    @Test
    void shouldReturnExpectedCommentary() throws Exception {
        Mono<Commentary> expectedCommentary = Mono.just(firstCommentary);
        when(commentaryRepository.findById(firstCommentary.getId())).thenReturn(expectedCommentary);

        webTestClient.get()
                .uri("/api/comments/" + firstCommentary.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Commentary.class)
                .value(commentary -> {
                    assertThat(commentary).isEqualTo(firstCommentary);
                });
    }

    @DisplayName("Меняем текст комментария")
    @Test
    void shouldChangeCommentaryText() throws Exception {
        String expectedText = "Test Commentary";
        Commentary commentary = firstCommentary;
        commentary.setText(expectedText);

        Mono<Commentary> expectedCommentary = Mono.just(commentary);

        when(commentaryRepository.save(commentary)).thenReturn(expectedCommentary);

        webTestClient.patch()
                .uri("/api/comments/" + firstCommentary.getId())
                .body(expectedCommentary, Commentary.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Commentary.class)
                .value(resultCommentary -> {
                    assertThat(resultCommentary).isEqualTo(commentary);
                });
    }

    @DisplayName("Удаляем комментарий")
    @Test
    void shouldDeleteCommentary() throws Exception {
        Flux<Commentary> expectedCommentaries = Flux.just(firstCommentary);

        when(commentaryRepository.deleteById(firstCommentary.getId())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/comments/" + firstCommentary.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @DisplayName("Добавляем комментарий")
    @Test
    void shouldAddCommentary() throws Exception {
        when(commentaryRepository.save(firstCommentary)).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/api/comments")
                .body(Mono.just(firstCommentary), Commentary.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }
}