package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.spring.models.Author;

import java.util.List;
import static org.assertj.core.api.Assertions.*;

@DataMongoTest
@DisplayName("Репозиторий для работы с авторами")
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository repository;

    private final List<Author> expectedAuthors =
            List.of(new Author("John", "Tolkien"), new Author("Leo", "Tolstoy"));

    @DisplayName("Получаем авторов")
    @Test
    void shouldReturnExpectedAuthors() {
        Flux<Author> authors = repository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        StepVerifier
                .create(authors)
                .assertNext(author -> assertThat(author.getFullName()).isEqualTo(expectedAuthors.get(0).getFullName()))
                .assertNext(author -> assertThat(author.getFullName()).isEqualTo(expectedAuthors.get(1).getFullName()))
                .expectComplete()
                .verify();
    }
}