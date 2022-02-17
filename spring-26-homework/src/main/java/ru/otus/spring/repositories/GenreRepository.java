package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.models.GenreJPA;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<GenreJPA, Long> {
    Optional<GenreJPA> getGenreByNameIgnoreCase(String name);
}
