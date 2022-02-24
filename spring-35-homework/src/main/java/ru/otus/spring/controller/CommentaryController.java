package ru.otus.spring.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    @HystrixCommand(fallbackMethod = "editCommentaryFallback")
    public String editCommentary(@RequestParam("id") String id, Model model) {
        Commentary commentary = commentaryService.getCommentaryById(id);
        model.addAttribute("commentary", commentary);
        return "commedit";
    }

    private String editCommentaryFallback(String id, Model model) {
        return "fallbackpage";
    }

    @PostMapping("/commedit")
    @HystrixCommand(fallbackMethod = "saveCommentaryFallback")
    public String saveCommentary(@RequestParam("id") String id, String text, Model model) {
        commentaryService.changeCommentaryTextById(id, text);
        Commentary commentary = commentaryService.getCommentaryById(id);
        model.addAttribute("commentary", commentary);
        return "redirect:/bookedit?id=" + commentary.getBook().getId();
    }

    private String saveCommentaryFallback(String id, String text, Model model) {
        return "fallbackpage";
    }

    @PostMapping("/commdel")
    @HystrixCommand(fallbackMethod = "deleteCommentaryFallback")
    public String deleteCommentary(String commentaryId, String bookId, Model model) {
        commentaryService.deleteByCommentaryId(commentaryId);
        return "redirect:/bookedit?id=" + bookId;
    }

    private String deleteCommentaryFallback(String commentaryId, String bookId, Model model) {
        return "fallbackpage";
    }

    @GetMapping("/commadd")
    @HystrixCommand(fallbackMethod = "addCommentaryFallback")
    public String addCommentary(@RequestParam String bookId, Model model) {
        model.addAttribute("book", bookService.getBookById(bookId));
        return "commadd";
    }

    private String addCommentaryFallback(String bookId, Model model) {
        return "fallbackpage";
    }

    @PostMapping("/commadd")
    @HystrixCommand(fallbackMethod = "addCommentaryFallback")
    public String addCommentary(@RequestParam String bookId, @RequestParam String text, Model model) {
        commentaryService.addNewCommentary(bookService.getBookById(bookId), text);
        return "redirect:/bookedit?id=" + bookId;
    }

    private String addCommentaryFallback(String bookId, String text, Model model) {
        return "fallbackpage";
    }
}
