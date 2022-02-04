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

    @GetMapping("/api/bookcomments/{bookId}")
    public List<Commentary> getAllBookCommentaries(@PathVariable String bookId) {
            return commentaryService.getCommentariesByBookId(bookId);
    }

    @GetMapping("/api/comments/{id}")
    public Commentary editCommentary(@PathVariable String id) {
        return commentaryService.getCommentaryById(id);
    }

    @PutMapping("/api/comments")
    public ResponseEntity saveCommentary(@RequestBody Commentary commentary) {
        commentaryService.changeCommentaryTextById(commentary.getId(), commentary.getText());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/comments")
    public ResponseEntity deleteCommentary(@RequestBody String id) {
        commentaryService.deleteByCommentaryId(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/comments")
    public ResponseEntity addCommentary(@RequestBody Commentary commentary) {
        commentaryService.addNewCommentary(commentary.getBook(), commentary.getText());
        return ResponseEntity.ok().build();
    }
}
