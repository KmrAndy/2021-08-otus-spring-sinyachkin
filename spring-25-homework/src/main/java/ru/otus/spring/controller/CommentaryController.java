package ru.otus.spring.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentaryService;

@Controller
public class CommentaryController {
    private final CommentaryService commentaryService;
    private final BookService bookService;

    public CommentaryController(CommentaryService commentaryService, BookService bookService) {
        this.commentaryService = commentaryService;
        this.bookService = bookService;
    }

    @GetMapping("/commedit")
    public String editCommentary(@RequestParam("id") String id, Model model) {
        Commentary commentary = commentaryService.getCommentaryById(id);
        model.addAttribute("commentary", commentary);
        return "commedit";
    }

    @PostMapping("/commedit")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveCommentary(@RequestParam("id") String id, String text, Model model) {
        commentaryService.changeCommentaryTextById(id, text);
        Commentary commentary = commentaryService.getCommentaryById(id);
        model.addAttribute("commentary", commentary);
        return "redirect:/bookedit?id=" + commentary.getBook().getId();
    }

    @PostMapping("/commdel")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCommentary(String commentaryId, String bookId, Model model) {
        commentaryService.deleteByCommentaryId(commentaryId);
        return "redirect:/bookedit?id=" + bookId;
    }

    @GetMapping("/commadd")
    public String addBook(@RequestParam String bookId, Model model) {
        model.addAttribute("book", bookService.getBookById(bookId));
        return "commadd";
    }

    @PostMapping("/commadd")
    public String addBook(@RequestParam String bookId, @RequestParam String text, Model model) {
        commentaryService.addNewCommentary(bookService.getBookById(bookId), text);
        return "redirect:/bookedit?id=" + bookId;
    }
}
