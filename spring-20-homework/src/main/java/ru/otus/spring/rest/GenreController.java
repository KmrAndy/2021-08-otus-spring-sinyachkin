package ru.otus.spring.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.GenreRepository;

@RestController
public class GenreController {
    private final GenreRepository repository;

    public GenreController(GenreRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/genres")
    public Flux<Genre> getAllGenres() {
        return repository.findAll();
    }
}
