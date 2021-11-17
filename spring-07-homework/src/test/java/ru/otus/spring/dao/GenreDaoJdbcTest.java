package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dao для работы с жанрами")
@JdbcTest
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {

    @Autowired
    private GenreDaoJdbc dao;

    @DisplayName("Получаем жанр по его названию")
    @Test
    void shouldReturnGenreByName() {
        Genre expectedGenre = new Genre(1L, "fantasy");

        Genre actualGenre= dao.getGenreByName(expectedGenre.getName());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }
}