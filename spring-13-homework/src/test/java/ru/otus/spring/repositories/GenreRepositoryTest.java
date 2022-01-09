package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@DisplayName("Репозиторий для работы с жанрами")
@EnableConfigurationProperties
@ComponentScan({"ru.otus.spring.config", "ru.otus.spring.repositories"})
class GenreRepositoryTest {

    @Autowired
    private GenreRepository repository;

    @DisplayName("Получаем жанр по его названию")
    @Test
    void shouldReturnGenreByName() {
        Genre expectedGenre = new Genre("fantasy");
        Optional<Genre> actualGenre= repository.getGenreByNameIgnoreCase("fantasy");
        
        assertThat(actualGenre.isPresent()).isEqualTo(true);
        assertThat(actualGenre.get().getName()).isEqualTo(expectedGenre.getName());
    }
}