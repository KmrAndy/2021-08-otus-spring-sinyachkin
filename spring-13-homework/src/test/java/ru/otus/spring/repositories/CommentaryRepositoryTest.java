package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataMongoTest
@DisplayName("Репозиторий для работы с комментариями")
class CommentaryRepositoryTest {
    private static final Long EXPECTED_COMMENTARY_COUNT = 1L;

    @Autowired
    private CommentaryRepository commentaryRepository;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("Получаем количество комментариев у книги")
    @Test
    void shouldReturnExpectedCommentaryCount() {
        Book book = bookRepository.findAll().get(0);
        Long actualCount = commentaryRepository.countByBook(book.getId());
        assertThat(actualCount).isEqualTo(EXPECTED_COMMENTARY_COUNT);
    }

    @DisplayName("Добавляем новый комментарий")
    @Test
    void shouldInsertCommentary() {
        Book book = bookRepository.findAll().get(0);
        Commentary expectedCommentary = new Commentary(book, "New commentary");

        Commentary actualCommentary = commentaryRepository.save(expectedCommentary);

        assertThat(actualCommentary.getBook()).isNotNull().usingRecursiveComparison().isEqualTo(expectedCommentary.getBook());
        assertThat(actualCommentary.getText()).isEqualTo(expectedCommentary.getText());

        commentaryRepository.delete(actualCommentary);
    }

    @DisplayName("Изменяем текст комментария по его id")
    @Test
    void shouldUpdateCommentaryById() {
        Book book = bookRepository.findAll().get(0);
        Commentary expectedCommentary = commentaryRepository.findAllByBook(book.getId()).get(0);
        String expectedCommentaryText = "New Commentary text";

        int result = commentaryRepository.updateTextById(expectedCommentary.getId(), expectedCommentaryText);
        Optional<Commentary> actualCommentary = commentaryRepository.findById(expectedCommentary.getId());

        assertThat(result).isEqualTo(1);
        assertThat(actualCommentary.get().getText()).isEqualTo(expectedCommentaryText);
    }

    @DisplayName("Получаем комментарий по его id")
    @Test
    void shouldReturnCommentaryById() {
        Book book = bookRepository.findAll().get(0);
        Commentary expectedCommentary = commentaryRepository.findAllByBook(book.getId()).get(0);
        Optional<Commentary> actualCommentary = commentaryRepository.findById(expectedCommentary.getId());

        assertThat(actualCommentary.get()).isNotNull().usingRecursiveComparison().isEqualTo(expectedCommentary);
        assertThat(actualCommentary.get().getBook()).usingRecursiveComparison().isEqualTo(expectedCommentary.getBook());
    }

    @DisplayName("Удаляем комментарий по его id")
    @Test
    void shouldDeleteByCommentaryId() {
        Book book = bookRepository.findAll().get(0);
        Commentary expectedCommentary = commentaryRepository.findAllByBook(book.getId()).get(0);

        commentaryRepository.deleteById(expectedCommentary.getId());

        Optional<Commentary> actualCommentary = commentaryRepository.findById(expectedCommentary.getId());
        assertThat(actualCommentary).isEmpty();

        expectedCommentary.setId(null);
        commentaryRepository.save(expectedCommentary);
    }
}