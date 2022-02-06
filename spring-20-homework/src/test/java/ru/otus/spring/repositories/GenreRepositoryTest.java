package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.spring.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@DisplayName("Репозиторий для работы с жанрами")
class GenreRepositoryTest {

    @Autowired
    private GenreRepository repository;

    private final List<Genre> expectedGenres =
            List.of(new Genre("fantasy"), new Genre("novel"));

    @DisplayName("Получаем жанры")
    @Test
    void shouldReturnGenreByName() {
        Flux<Genre> genres = repository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        StepVerifier
                .create(genres)
                .assertNext(genre -> assertThat(genre.getName()).isEqualTo(expectedGenres.get(0).getName()))
                .assertNext(genre -> assertThat(genre.getName()).isEqualTo(expectedGenres.get(1).getName()))
                .expectComplete()
                .verify();
    }
}