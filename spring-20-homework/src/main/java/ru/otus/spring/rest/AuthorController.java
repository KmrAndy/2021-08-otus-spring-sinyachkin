package ru.otus.spring.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.spring.models.Author;
import ru.otus.spring.repositories.AuthorRepository;

@RestController
public class AuthorController {
    private AuthorRepository repository;

    public AuthorController(AuthorRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/authors")
    public Flux<Author> getAllAuthors() {
        return repository.findAll();
    }
}
