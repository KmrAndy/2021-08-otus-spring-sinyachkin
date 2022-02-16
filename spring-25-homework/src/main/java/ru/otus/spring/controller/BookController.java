package ru.otus.spring.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    public BookController(AuthorService authorService, GenreService genreService,
                          BookService bookService, CommentaryService commentaryService) {
        this.bookService = bookService;
        this.commentaryService = commentaryService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @GetMapping("/booklist")
    public String listBook(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "booklist";
    }

    @GetMapping("/bookedit")
    public String editBook(@RequestParam("id") String id, Model model) {
        Book book = bookService.getBookById(id);
        List<Commentary> commentaries = commentaryService.getCommentariesByBookId(id);
        model.addAttribute("book", book);
        model.addAttribute("commentaries", commentaries);
        return "bookedit";
    }
    
    @PostMapping("/bookedit")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveBook(Book book, Model model) {
        bookService.changeBookNameByBookId(book.getId(), book.getName());
        model.addAttribute("book", book);
        return "redirect:/booklist";
    }

    @PostMapping("/bookdel")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteBook(String bookId, Model model) {
        bookService.deleteByBookId(bookId);
        return "redirect:/booklist";
    }

    @GetMapping("/bookadd")
    @PreAuthorize("hasRole('ADMIN')")
    public String addBook(Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("genres", genreService.getAllGenres());
        return "bookadd";
    }

    @PostMapping("/bookadd")
    @PreAuthorize("hasRole('ADMIN')")
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
        return "redirect:/booklist";
    }
}
