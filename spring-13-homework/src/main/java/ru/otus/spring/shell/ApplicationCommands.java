package ru.otus.spring.shell;

import org.springframework.dao.DataAccessException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.models.Book;
import ru.otus.spring.exception.*;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.CommentaryService;
import ru.otus.spring.service.MessageService;

import java.util.List;

@ShellComponent
public class ApplicationCommands {
    private final MessageService messageService;
    private final BookService bookService;
    private final CommentaryService commentaryService;

    public ApplicationCommands(MessageService messageService,
                               BookService bookService,
                               CommentaryService commentaryService){
        this.messageService = messageService;
        this.bookService = bookService;
        this.commentaryService = commentaryService;
    }

    @ShellMethod(value = "Books count command", key = {"count"})
    public String count() {
        try {
            return messageService.getMessage(
                    "strings.books-count-label", String.valueOf(bookService.getBooksCount()));
        } catch(DataAccessException e){
            return messageService.getMessage("strings.other-error-label");
        }
    }

    @ShellMethod(value = "Change book name by book id", key = {"chngbyid"})
    public String changeBookNameByBookId(String id, String newName) {
        try {
            bookService.changeBookNameByBookId(id, newName);
            return messageService.getMessage(
                    "strings.change-book-name", String.valueOf(id), newName);
        } catch(DataAccessException e){
            if (e.contains(NoBookFoundException.class)){
                return messageService.getMessage(
                        "strings.no-book-found-label", String.valueOf(id));
            } else {
                return messageService.getMessage("strings.other-error-label");
            }
        }
    }

    @ShellMethod(value = "Get book by book id", key = {"getbookbyid"})
    public String getBookById(String id) {
        try{
            return bookService.getBookInfoById(id);
        } catch(DataAccessException e){
            if (e.contains(NoBookFoundException.class)){
                return messageService.getMessage(
                        "strings.no-book-found-label", String.valueOf(id));
            } else {
                return messageService.getMessage("strings.other-error-label");
            }
        }
    }

    @ShellMethod(value = "Get all books", key = {"getall"})
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @ShellMethod(value = "Delete book by id", key = {"delbook"})
    public String deleteByBookId(String id) {
        try{
            bookService.deleteByBookId(id);
            return messageService.getMessage(
                    "strings.delete-book-label", String.valueOf(id));
        } catch(DataAccessException e){
            if (e.contains(NoBookFoundException.class)){
                return messageService.getMessage(
                        "strings.no-book-found-label", String.valueOf(id));
            } else {
                return messageService.getMessage("strings.other-error-label");
            }
        }
    }

    @ShellMethod(value = "Add new book", key = {"addbook"})
    public String addNewBook(String bookName, String authorFirstName, String authorLastName, String genreName){
        try {
            Book book = bookService.addNewBook(bookName, authorFirstName, authorLastName, genreName);
            return messageService.getMessage(
                    "strings.add-book-label", bookName, String.valueOf(book.getId()));
        } catch(DataAccessException e) {
            if (e.contains(NoAuthorFoundException.class)) {
                return messageService.getMessage(
                        "strings.no-author-found-label", authorFirstName, authorLastName);
            } else if (e.contains(NoGenreFoundException.class)) {
                return messageService.getMessage(
                        "strings.no-genre-found-label", genreName);
            } else {
                return messageService.getMessage("strings.other-error-label");
            }
        }
    }

    @ShellMethod(value = "Book commentaries count command", key = {"commcount"})
    public String commentariesCount(String bookId) {
        try {
            return messageService.getMessage(
                    "strings.commentaries-count-label", String.valueOf(commentaryService.getBookCommentariesCount(bookId)));
        } catch(DataAccessException e) {
            if (e.contains(NoBookFoundException.class)){
                return messageService.getMessage(
                        "strings.no-book-found-label", String.valueOf(bookId));
            } else {
                return messageService.getMessage("strings.other-error-label");
            }
        }
    }

    @ShellMethod(value = "Change commentary text by commentary id", key = {"chngtextbyid"})
    public String changeCommentaryTextByCommentaryId(String id, String newText) {
        try {
            commentaryService.changeCommentaryTextById(id, newText);
            return messageService.getMessage(
                    "strings.change-commentary-text", String.valueOf(id), newText);
        } catch(DataAccessException e){
            if (e.contains(NoCommentaryFoundException.class)){
                return messageService.getMessage(
                        "strings.no-commentary-found-label", String.valueOf(id));
            } else {
                return messageService.getMessage("strings.other-error-label");
            }
        }
    }

    @ShellMethod(value = "Get Commentary text by commentary id", key = {"getcommbyid"})
    public String getCommentaryById(String id) {
        try{
            return commentaryService.getCommentaryTextById(id);
        } catch(DataAccessException e){
            if (e.contains(NoCommentaryFoundException.class)){
                return messageService.getMessage(
                        "strings.no-commentary-found-label", String.valueOf(id));
            } else {
                return messageService.getMessage("strings.other-error-label");
            }
        }
    }

    @ShellMethod(value = "Get Book Commentaries", key = {"getbookcomms"})
    public List<String> getBookCommentaries(String bookId) {
        try{
            return commentaryService.getCommentariesTextByBookId(bookId);
        } catch(DataAccessException e) {
            if (e.contains(NoBookFoundException.class)){
                return List.of(messageService.getMessage(
                        "strings.no-book-found-label", String.valueOf(bookId)));
            } else {
                return List.of(messageService.getMessage("strings.other-error-label"));
            }
        }
    }

    @ShellMethod(value = "Delete commentary by id", key = {"delcomm"})
    public String deleteByCommentaryId(String id) {
        try{
            commentaryService.deleteByCommentaryId(id);
            return messageService.getMessage(
                    "strings.delete-commentary-label", String.valueOf(id));
        } catch(DataAccessException e){
            if (e.contains(NoCommentaryFoundException.class)){
                return messageService.getMessage(
                        "strings.no-commentary-found-label", String.valueOf(id));
            } else {
                return messageService.getMessage("strings.other-error-label");
            }
        }
    }

    @ShellMethod(value = "Add new book commentary", key = {"addcomm"})
    public String addNewBookCommentary(String bookId, String text){
        try {
            Commentary commentary = bookService.addBookCommentary(bookId, text);
            return messageService.getMessage(
                    "strings.add-commentary-label", text, String.valueOf(commentary.getId()));
        } catch(DataAccessException e) {
            if (e.contains(NoBookFoundException.class)){
                return messageService.getMessage(
                        "strings.no-book-found-label", String.valueOf(bookId));
            } else {
                return messageService.getMessage("strings.other-error-label");
            }
        }
    }
}
