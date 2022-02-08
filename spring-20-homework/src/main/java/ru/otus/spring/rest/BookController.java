package ru.otus.spring.rest;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.models.Book;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentaryRepository;

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

    @PatchMapping("/api/books/{id}")
    public Mono<Book> saveBook(@PathVariable String id, @RequestBody Book book) {
        return Mono.zip(bookRepository.save(book),
                        commentaryRepository.updateCommentariesBookByBook(book),
                        ((book1, updateResult) -> book1));
    }

    @DeleteMapping("/api/books/{id}")
    public Mono<Void> deleteBook(@PathVariable String id) {
        return Mono.zip(bookRepository.deleteById(id),
                        commentaryRepository.deleteCommentariesByBookId(id),
                ((unused, deleteResult) -> unused));
    }

    @PostMapping("/api/books")
    public Mono<Book> addBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }
}
