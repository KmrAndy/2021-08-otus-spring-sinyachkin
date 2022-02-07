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

    @GetMapping("/api/comments/book/{bookId}")
    public List<Commentary> getAllBookCommentaries(@PathVariable String bookId) {
            return commentaryService.getCommentariesByBookId(bookId);
    }

    @GetMapping("/api/comments/{id}")
    public Commentary editCommentary(@PathVariable String id) {
        return commentaryService.getCommentaryById(id);
    }

    @PutMapping("/api/comments/{id}/{newText}")
    public ResponseEntity<Commentary> saveCommentary(@PathVariable String id, @PathVariable String newText) {
        commentaryService.changeCommentaryTextById(id, newText);
        return ResponseEntity.ok(commentaryService.getCommentaryById(id));
    }

    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<Void> deleteCommentary(@PathVariable String id) {
        commentaryService.deleteByCommentaryId(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/comments")
    public ResponseEntity<Void> addCommentary(@RequestBody Commentary commentary) {
        commentaryService.addNewCommentary(commentary.getBook(), commentary.getText());
        return ResponseEntity.ok().build();
    }
}
