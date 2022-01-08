package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.models.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с жанрами")
@DataJpaTest
class GenreRepositoryJpaTest {

    @Autowired
    private GenreRepository repository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Получаем жанр по его названию")
    @Test
    void shouldReturnGenreByName() {
        Optional<Genre> actualGenre= repository.getGenreByNameIgnoreCase("fantasy");
        Genre expectedGenre = em.find(Genre.class, 1L);

        assertThat(actualGenre.get()).usingRecursiveComparison().isEqualTo(expectedGenre);
    }
}