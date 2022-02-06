package ru.otus.spring.rest;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.repositories.CommentaryRepository;


@RestController
public class CommentaryController {
    private final CommentaryRepository repository;

    public CommentaryController(CommentaryRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/comments/book/{bookId}")
    public Flux<Commentary> getAllBookCommentaries(@PathVariable String bookId) {
            return repository.findAllByBook(bookId);
    }

    @GetMapping("/api/comments/{id}")
    public Mono<Commentary> editCommentary(@PathVariable String id) {
        return repository.findById(id);
    }

    @PutMapping("/api/comments/{id}/{newText}")
    public Mono<Commentary> saveCommentary(@PathVariable String id, @PathVariable String newText) {
        Mono<Commentary> commentary = repository.findById(id)
                .map(foundCommentary -> {foundCommentary.setText(newText); return foundCommentary;});

        repository.saveAll(commentary)
                .subscribe();

        return commentary;
    }

    @DeleteMapping("/api/comments/{id}")
    public Mono<Void> deleteCommentary(@PathVariable String id) {
        repository.deleteById(id)
                .subscribe();

        return Mono.empty();
    }

    @PostMapping("/api/comments")
    public Mono<Commentary> addCommentary(@RequestBody Commentary commentary) {
        repository.save(commentary)
                .subscribe();

        return Mono.empty();
    }
}
