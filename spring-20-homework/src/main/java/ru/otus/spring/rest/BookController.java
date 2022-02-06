package ru.otus.spring.rest;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentaryRepository;

import java.util.Objects;

@RestController
public class BookController {
    private final BookRepository bookRepository;
    private final CommentaryRepository commentaryRepository;

    public BookController(BookRepository bookRepository, CommentaryRepository commentaryRepository) {
        this.bookRepository = bookRepository;
        this.commentaryRepository = commentaryRepository;
    }

    @GetMapping("/api/books")
    public Flux<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/api/books/{id}")
    public Mono<Book> editBook(@PathVariable String id) {
        return bookRepository.findById(id);
    }

    @PutMapping("/api/books/{id}/{newName}")
    public Mono<Book> saveBook(@PathVariable String id, @PathVariable String newName) {
        Mono<Book> book = bookRepository.findById(id)
                .map(foundBook -> {foundBook.setName(newName); return foundBook;});

        bookRepository.saveAll(book)
                .subscribe();

        Flux<Commentary> commentaries = commentaryRepository.findAllByBook(id)
                .map(commentary -> {commentary.getBook().setName(newName); return commentary;});

        commentaryRepository.saveAll(commentaries)
                .subscribe();

        return book;
    }

    @DeleteMapping("/api/books/{id}")
    public Mono<Void> deleteBook(@PathVariable String id) {
        bookRepository.deleteById(id)
                .subscribe();

        Flux<Commentary> commentaries = commentaryRepository.findAllByBook(id);

        commentaryRepository.deleteAll(commentaries)
                .subscribe();

        return Mono.empty();
    }

    @PostMapping("/api/books")
    public Mono<Void> addBook(@RequestBody Book book) {
        bookRepository.save(book)
                .subscribe();
        return Mono.empty();
    }
}
