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
import ru.otus.spring.models.Author;
import ru.otus.spring.repositories.AuthorRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebFluxTest(AuthorController.class)
@AutoConfigureDataMongo
@DisplayName("Тестирование рест контроллера авторов")
class AuthorControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @SpyBean
    private AuthorRepository authorRepository;

    private final Author firstAuthor = new Author("1", "John", "Tolkien");
    private final Author secondAuthor = new Author("2", "Leo", "Tolstoy");

    @DisplayName("Получаем всех авторов")
    @Test
    void shouldReturnAllAuthors() throws Exception {
        Flux<Author> expectedAuthors = Flux.just(firstAuthor, secondAuthor);

        when(authorRepository.findAll()).thenReturn(expectedAuthors);

        webTestClient.get()
                .uri("/api/authors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Author.class)
                .value(authors -> {
                    assertThat(authors.get(0).getId()).isEqualTo(firstAuthor.getId());
                    assertThat(authors.get(0).getFirstName()).isEqualTo(firstAuthor.getFirstName());
                    assertThat(authors.get(0).getLastName()).isEqualTo(firstAuthor.getLastName());
                    assertThat(authors.get(1).getId()).isEqualTo(secondAuthor.getId());
                    assertThat(authors.get(1).getFirstName()).isEqualTo(secondAuthor.getFirstName());
                    assertThat(authors.get(1).getLastName()).isEqualTo(secondAuthor.getLastName());
                });
    }
}