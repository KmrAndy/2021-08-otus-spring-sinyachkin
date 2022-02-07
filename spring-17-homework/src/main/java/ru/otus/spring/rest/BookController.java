package ru.otus.spring.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.models.Book;
import ru.otus.spring.service.BookService;

import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/api/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/api/books/{id}")
    public Book editBook(@PathVariable String id) {
        return bookService.getBookById(id);
    }
    
    @PutMapping("/api/books/{id}/{newName}")
    public ResponseEntity<Book> saveBook(@PathVariable String id, @PathVariable String newName) {
        bookService.changeBookNameByBookId(id, newName);
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookService.deleteByBookId(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/books")
    public ResponseEntity<Void> addBook(@RequestBody Book book) {
        bookService.addNewBook(book.getName(), book.getAuthors(), book.getGenres());
        return ResponseEntity.ok().build();
    }
}
