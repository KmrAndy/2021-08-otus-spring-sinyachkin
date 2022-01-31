package ru.otus.spring.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.service.CommentaryService;

import java.util.List;

@RestController
public class CommentaryController {
    private final CommentaryService commentaryService;

    public CommentaryController(CommentaryService commentaryService) {
        this.commentaryService = commentaryService;
    }

    @GetMapping("/api/commlist/{bookId}")
    public List<Commentary> getAllBookCommentaries(@PathVariable String bookId) {
            return commentaryService.getCommentariesByBookId(bookId);
    }

    @GetMapping("/api/commedit/{id}")
    public Commentary editCommentary(@PathVariable String id) {
        return commentaryService.getCommentaryById(id);
    }

    @PostMapping("/api/commedit")
    public ResponseEntity saveCommentary(@RequestBody Commentary commentary) {
        commentaryService.changeCommentaryTextById(commentary.getId(), commentary.getText());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/commdel")
    public ResponseEntity deleteCommentary(@RequestBody String id) {
        commentaryService.deleteByCommentaryId(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/commadd")
    public ResponseEntity addCommentary(@RequestBody Commentary commentary) {
        commentaryService.addNewCommentary(commentary.getBook(), commentary.getText());
        return ResponseEntity.ok().build();
    }
}
