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

    @GetMapping("/api/booklist")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/api/bookedit/{id}")
    public Book editBook(@PathVariable String id) {
        return bookService.getBookById(id);
    }
    
    @PostMapping("/api/bookedit/")
    public ResponseEntity saveBook(@RequestBody Book book) {
        bookService.changeBookNameByBookId(book.getId(), book.getName());
        return ResponseEntity.ok(bookService.getBookById(book.getId()));
    }

    @PostMapping("/api/bookdel")
    public ResponseEntity deleteBook(@RequestBody Book book) {
        bookService.deleteByBookId(book.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/bookadd")
    public ResponseEntity addBook(@RequestBody Book book) {
        bookService.addNewBook(book.getName(), book.getAuthors(), book.getGenres());
        return ResponseEntity.ok().build();
    }
}
