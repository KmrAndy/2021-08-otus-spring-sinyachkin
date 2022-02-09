package ru.otus.spring.repositories;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataMongoTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Репозиторий для работы с комментариями")
class CommentaryRepositoryTest {
    private static final Long EXPECTED_COMMENTARY_COUNT = 1L;

    @Autowired
    private CommentaryRepository commentaryRepository;

    @Autowired
    private BookRepository bookRepository;

    private Book expectedBook;

    @BeforeEach
    private void init(){
        expectedBook = bookRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .collectList()
                .block()
                .get(0);
    }

    @DisplayName("Добавляем новый комментарий")
    @Order(1)
    @Test
    void shouldInsertCommentary() {
        Commentary expectedCommentary = new Commentary(expectedBook, "New commentary");

        Mono<Commentary> actualCommentary = commentaryRepository.save(expectedCommentary);

        StepVerifier
                .create(actualCommentary)
                .assertNext(commentary -> {
                    assertThat(commentary).isNotNull();
                    assertThat(commentary.getText()).isEqualTo(expectedCommentary.getText());
                    assertThat(commentary.getBook()).usingRecursiveComparison().isEqualTo(expectedCommentary.getBook());
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("Изменяем текст комментария по его id")
    @Order(2)
    @Test
    void shouldUpdateCommentaryById() {
        Commentary expectedCommentary = commentaryRepository.findAllByBook(expectedBook.getId())
                .collectList()
                .block()
                .get(0);
        String expectedText = "New Commentary text";
        expectedCommentary.setText(expectedText);

        Mono<Commentary> actualCommentary = commentaryRepository.save(expectedCommentary);

        StepVerifier
                .create(actualCommentary)
                .assertNext(commentary -> assertThat(commentary.getText()).isEqualTo(expectedText))
                .expectComplete()
                .verify();
    }

    @DisplayName("Получаем комментарий по его id")
    @Order(3)
    @Test
    void shouldReturnCommentaryById() {
        Commentary expectedCommentary = commentaryRepository.findAllByBook(expectedBook.getId())
                .collectList()
                .block()
                .get(0);

        Mono<Commentary> actualCommentary = commentaryRepository.findById(expectedCommentary.getId());

        StepVerifier
                .create(actualCommentary)
                .assertNext(commentary -> assertThat(commentary).isNotNull().usingRecursiveComparison().isEqualTo(expectedCommentary))
                .expectComplete()
                .verify();
    }

    @DisplayName("Удаляем комментарий по его id")
    @Order(4)
    @Test
    void shouldDeleteByCommentaryId() {
        Commentary expectedCommentary = commentaryRepository.findAllByBook(expectedBook.getId())
                .collectList()
                .block()
                .get(0);

        Mono<Void> deleteResult = commentaryRepository.deleteById(expectedCommentary.getId());

        StepVerifier
                .create(deleteResult)
                .expectNextCount(0)
                .verifyComplete();

        Mono<Commentary> actualCommentary = commentaryRepository.findById(expectedCommentary.getId());

        StepVerifier
                .create(actualCommentary)
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }
}