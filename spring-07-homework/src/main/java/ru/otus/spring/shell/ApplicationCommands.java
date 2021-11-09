package ru.otus.spring.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.domain.Book;
import ru.otus.spring.exception.*;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.MessageService;

import java.util.List;

@ShellComponent
public class ApplicationCommands {
    private final MessageService messageService;
    private final BookService bookService;

    public ApplicationCommands(MessageService messageService, BookService bookService){
        this.messageService = messageService;
        this.bookService = bookService;
    }

    @ShellMethod(value = "Books count command", key = {"count"})
    public String count() {
        try {
            return messageService.getMessage(
                    "strings.books-count-label", String.valueOf(bookService.getBooksCount()));
        } catch(LibraryAccessException e){
            return messageService.getMessage("strings.other-error-label");
        }
    }

    @ShellMethod(value = "Change book name by book id", key = {"chngbyid"})
    public String changeBookNameByBookId(long id, String newName) {
        try {
            bookService.changeBookNameByBookId(id, newName);
            return messageService.getMessage(
                    "strings.change-book-name", String.valueOf(id), newName);
        } catch(LibraryAccessException e){
            if (e.contains(NoBookFoundException.class)){
                return messageService.getMessage(
                        "strings.no-book-found-label", String.valueOf(id));
            } else {
                return messageService.getMessage("strings.other-error-label");
            }
        }
    }

    @ShellMethod(value = "Get book by book id", key = {"getbookbyid"})
    public String getBookById(long id) {
        try{
            return bookService.getBookInfoById(id);
        } catch(LibraryAccessException e){
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
    public String deleteByBookId(long id) {
        try{
            bookService.deleteByBookId(id);
            return messageService.getMessage(
                    "strings.delete-book-label", String.valueOf(id));
        } catch(LibraryAccessException e){
            if (e.contains(NoBookFoundException.class)){
                return messageService.getMessage(
                        "strings.no-book-found-label", String.valueOf(id));
            } else {
                return messageService.getMessage("strings.other-error-label");
            }
        }
    }

    @ShellMethod(value = "Add new book", key = {"addbook"})
    public String addNewBook(long id, String bookName, String authorFirstName, String authorLastName, String genreName){
        try {
            bookService.addNewBook(id, bookName, authorFirstName, authorLastName, genreName);
            return messageService.getMessage(
                    "strings.add-book-label", bookName, String.valueOf(id));
        } catch(LibraryAccessException e){
            if (e.contains(NoAuthorFoundException.class)){
                return messageService.getMessage(
                        "strings.no-author-found-label", authorFirstName, authorLastName);
            } else if (e.contains(NoGenreFoundException.class)){
                return messageService.getMessage(
                        "strings.no-genre-found-label", genreName);
            } else if (e.contains(UniqueBookIdException.class)){
                return messageService.getMessage(
                        "strings.add-book-exception-label", String.valueOf(id));
            } else {
                return messageService.getMessage("strings.other-error-label");
            }
        }
    }
}
