package ru.otus.spring.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.models.Author;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с авторами")
@DataJpaTest
class AuthorRepositoryJpaTest {

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Получаем автора по его полному имени")
    @Test
    void shouldReturnAuthorByFullName() {
        Optional<Author> actualAuthor= repository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase("John", "Tolkien");
        Author expectedAuthor = em.find(Author.class,1L);
        assertThat(actualAuthor.get()).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }
}