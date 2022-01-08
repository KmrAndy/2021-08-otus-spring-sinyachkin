package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с комментариями")
@DataJpaTest
class CommentaryRepositoryJpaTest {
    private static final Long EXPECTED_COMMENTARY_COUNT = 2L;

    @Autowired
    private CommentaryRepository repository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Получаем количество комментариев у книги")
    @Test
    void shouldReturnExpectedCommentaryCount() {
        Book book = em.find(Book.class, 1L);
        Long actualCount = repository.countByBook(book);
        assertThat(actualCount).isEqualTo(EXPECTED_COMMENTARY_COUNT);
    }

    @DisplayName("Добавляем новый комментарий")
    @Test
    void shouldInsertCommentary() {
        Book expectedBook = em.find(Book.class, 1L);
        Commentary expectedCommentary = em.find(Commentary.class, 5L);
        assertThat(expectedCommentary).isNull();

        expectedCommentary = new Commentary(5L, expectedBook, "New commentary");

        Commentary actualCommentary = repository.save(expectedCommentary);

        assertThat(actualCommentary).isNotNull().usingRecursiveComparison().isEqualTo(expectedCommentary);
        assertThat(actualCommentary.getBook()).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Изменяем текст комментария по его id")
    @Test
    void shouldUpdateCommentaryById() {
        Commentary expectedCommentary = em.find(Commentary.class, 1L);
        assertThat(expectedCommentary).isNotNull();

        String expectedCommentaryText = "New Commentary text";
        em.detach(expectedCommentary);

        repository.updateCommentaryById(expectedCommentary.getId(), expectedCommentaryText);

        Commentary actualCommentary = em.find(Commentary.class, 1L);
        assertThat(actualCommentary).isNotNull();
        assertThat(actualCommentary.getText()).isEqualTo(expectedCommentaryText);
    }

    @DisplayName("Получаем комментарий по его id")
    @Test
    void shouldReturnCommentaryById() {
        Commentary expectedCommentary = em.find(Commentary.class, 1L);
        Optional<Commentary> actualCommentary = repository.findById(expectedCommentary.getId());

        assertThat(actualCommentary.get()).isNotNull().usingRecursiveComparison().isEqualTo(expectedCommentary);
        assertThat(actualCommentary.get().getBook()).usingRecursiveComparison().isEqualTo(expectedCommentary.getBook());
    }

    @DisplayName("Получаем все комментарии для книги")
    @Test
    void shouldReturnAllCommentaryByBookId() {
        Book expectedBook = em.find(Book.class, 1L);
        Commentary expectedFirstCommentary = em.find(Commentary.class, 1L);
        Commentary expectedSecondCommentary = em.find(Commentary.class, 2L);

        List<Commentary> actualCommentaryList = repository.findAllByBook(expectedBook);
        assertThat(actualCommentaryList).isNotNull()
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedFirstCommentary, expectedSecondCommentary);
    }

    @DisplayName("Удаляем комментарий по его id")
    @Test
    void shouldDeleteByCommentaryId() {
        Commentary expectedCommentary = em.find(Commentary.class, 1L);
        assertThat(expectedCommentary).isNotNull();

        em.detach(expectedCommentary);
        repository.deleteById(expectedCommentary.getId());

        expectedCommentary = em.find(Commentary.class, 1L);
        assertThat(expectedCommentary).isNull();
    }
}