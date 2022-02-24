package ru.otus.spring.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.models.Genre;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentaryService;
import ru.otus.spring.service.GenreService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;
    private final CommentaryService commentaryService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final MeterRegistry meterRegistry;

    public BookController(AuthorService authorService, GenreService genreService,
                          BookService bookService, CommentaryService commentaryService,
                          MeterRegistry meterRegistry) {
        this.bookService = bookService;
        this.commentaryService = commentaryService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/booklist")
    @HystrixCommand(fallbackMethod = "listBookFallback")
    public String listBook(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "booklist";
    }

    private String listBookFallback(Model model) {
        return "fallbackpage";
    }

    @GetMapping("/bookedit")
    @HystrixCommand(fallbackMethod = "editBookFallback")
    public String editBook(@RequestParam("id") String id, Model model) {
        Book book = bookService.getBookById(id);
        List<Commentary> commentaries = commentaryService.getCommentariesByBookId(id);
        model.addAttribute("book", book);
        model.addAttribute("commentaries", commentaries);
        return "bookedit";
    }

    private String editBookFallback(String id, Model model) {
        return "fallbackpage";
    }
    
    @PostMapping("/bookedit")
    @HystrixCommand(fallbackMethod = "saveBookFallback")
    public String saveBook(@RequestParam("id") String id, String name, Model model) {
        bookService.changeBookNameByBookId(id, name);
        return "redirect:/booklist";
    }

    private String saveBookFallback(String id, String name, Model model) {
        return "fallbackpage";
    }

    @PostMapping("/bookdel")
    @HystrixCommand(fallbackMethod = "deleteBookFallback")
    public String deleteBook(String bookId, Model model) {
        bookService.deleteByBookId(bookId);
        return "redirect:/booklist";
    }

    private String deleteBookFallback(String bookId, Model model) {
        return "fallbackpage";
    }

    @GetMapping("/bookadd")
    @HystrixCommand(fallbackMethod = "addBookFallback")
    public String addBook(Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("genres", genreService.getAllGenres());
        return "bookadd";
    }

    private String addBookFallback(Model model) {
        return "fallbackpage";
    }

    @PostMapping("/bookadd")
    @HystrixCommand(fallbackMethod = "addBookFallback")
    public String addBook(@RequestParam String name, @RequestParam List<String> authorsId, @RequestParam List<String> genresId, Model model) {
        List<Author> authors = new ArrayList<>();
        List<Genre> genres = new ArrayList<>();

        for (String id: authorsId){
            authors.add(authorService.getAuthorById(id));
        }

        for (String id: genresId){
            genres.add(genreService.getGenreById(id));
        }

        bookService.addNewBook(name, authors, genres);
        meterRegistry.counter("newbooks.counter").increment();
        return "redirect:/booklist";
    }

    private String addBookFallback(String name, List<String> authorsId, List<String> genresId, Model model) {
        return "fallbackpage";
    }
}
