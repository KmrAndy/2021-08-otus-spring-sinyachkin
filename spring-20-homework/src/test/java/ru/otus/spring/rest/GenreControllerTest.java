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
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.GenreRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebFluxTest(GenreController.class)
@AutoConfigureDataMongo
@DisplayName("Тестирование рест контроллера жанров")
class GenreControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @SpyBean
    private GenreRepository genreRepository;

    private final Genre firstGenre = new Genre("1", "fantasy");
    private final Genre secondGenre = new Genre("2", "novel");

    @DisplayName("Получаем все жанры")
    @Test
    void shouldReturnAllGenres() throws Exception {
        Flux<Genre> expectedGenres = Flux.just(firstGenre, secondGenre);

        when(genreRepository.findAll()).thenReturn(expectedGenres);

        webTestClient.get()
                .uri("/api/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Genre.class)
                .value(genres -> {
                    assertThat(genres.get(0)).isEqualTo(firstGenre);
                    assertThat(genres.get(1)).isEqualTo(secondGenre);
                });
    }
}