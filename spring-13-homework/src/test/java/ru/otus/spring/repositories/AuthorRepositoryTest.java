package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.spring.models.Author;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@DisplayName("Репозиторий для работы с авторами")
@EnableConfigurationProperties
@ComponentScan({"ru.otus.spring.config", "ru.otus.spring.repositories"})
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository repository;

    @DisplayName("Получаем автора по его полному имени")
    @Test
    void shouldReturnAuthorByFullName() {
        Author expectedAuthor = new Author("John", "Tolkien");
        Optional<Author> actualAuthor = repository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase("John", "Tolkien");

        assertThat(actualAuthor.isPresent()).isEqualTo(true);
        assertThat(actualAuthor.get().getFirstName()).isEqualTo(expectedAuthor.getFirstName());
        assertThat(actualAuthor.get().getLastName()).isEqualTo(expectedAuthor.getLastName());
    }
}