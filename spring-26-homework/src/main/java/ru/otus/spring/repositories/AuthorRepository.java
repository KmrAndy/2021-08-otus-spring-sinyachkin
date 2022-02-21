package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.models.AuthorJPA;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<AuthorJPA, Long> {
    Optional<AuthorJPA> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
}
