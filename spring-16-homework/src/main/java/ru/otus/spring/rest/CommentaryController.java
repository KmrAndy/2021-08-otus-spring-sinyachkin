package ru.otus.spring.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.models.Genre;
import ru.otus.spring.service.CommentaryService;

import java.util.List;

@Controller
public class CommentaryController {
    private final CommentaryService commentaryService;

    public CommentaryController(CommentaryService commentaryService) {
        this.commentaryService = commentaryService;
    }

    @GetMapping("/commedit")
    public String editCommentary(@RequestParam("id") String id, Model model) {
        Commentary commentary = commentaryService.getCommentaryById(id);
        model.addAttribute("commentary", commentary);
        return "commedit";
    }

    @PostMapping("/commedit")
    public String saveCommentary(String commentaryId, String bookId, String text, Model model) {
        commentaryService.changeCommentaryTextById(commentaryId, text);
        model.addAttribute(commentaryService.getCommentaryById(commentaryId));
        return "redirect:/bookedit?id=" + bookId;
    }

    @PostMapping("/commdel")
    public String deleteCommentary(String commentaryId, String bookId, Model model) {
        commentaryService.deleteByCommentaryId(commentaryId);
        return "redirect:/bookedit?id=" + bookId;
    }

    @GetMapping("/commadd")
    public String addBook(@RequestParam Book book, Model model) {
        model.addAttribute("book", book);
        return "commadd";
    }

    @PostMapping("/commadd")
    public String addBook(@RequestParam Book book, @RequestParam String text, Model model) {
        commentaryService.addNewCommentary(book, text);
        return "redirect:/bookedit?id=" + book.getId();
    }
}
