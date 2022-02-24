package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.NoBookFoundException;
import ru.otus.spring.exception.OtherAccessException;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService{
    private final BookRepository repository;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CommentaryService commentaryService;

    public BookServiceImpl(BookRepository repository, AuthorService authorService,
                           GenreService genreService, CommentaryService commentaryService){
        this.repository = repository;
        this.authorService = authorService;
        this.genreService = genreService;
        this.commentaryService = commentaryService;
    }

    public long getBooksCount(){
        try {
            return repository.count();
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    @Transactional
    public Book addNewBook(String bookName, String authorFirstName, String authorLastName,String genreName)
            throws DataAccessException {
        try {
            return repository.save(
                    new Book(bookName,
                            List.of(authorService.getAuthorByName(authorFirstName, authorLastName)),
                            List.of(genreService.getGenreByName(genreName))));
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    @Transactional
    public Book addNewBook(String bookName, List<Author> authors, List<Genre> genres)
            throws DataAccessException{
        try {
            return repository.save(new Book(bookName, authors, genres));
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    @Transactional
    public void changeBookNameByBookId(String id, String newName) {
        try {
            Book Book = getBookById(id);
            Book.setName(newName);
            repository.save(Book);
            commentaryService.changeCommentariesBook(Book, commentaryService.getCommentariesByBookId(id));
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    public Book getBookById(String id) throws DataAccessException{
        Optional<Book> book;
        try {
            book = repository.findById(id);
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }

        if (book.isEmpty()) {
            throw new NoBookFoundException("There is no book with id '" + id + "'");
        }

        return book.get();
    }

    public String getBookInfoById(String id) throws DataAccessException{
        return String.valueOf(getBookById(id));
    }

    public List<Book> getAllBooks() throws DataAccessException{
        try {
            return repository.findAll();
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    @Transactional
    public void deleteByBookId(String id) throws DataAccessException{
        Book book = getBookById(id);
        try {
            repository.delete(book);
            commentaryService.deleteBookCommentaries(book);
        } catch (DataAccessException e){
            throw new OtherAccessException(e);
        }
    }

    @Transactional
    public Commentary addBookCommentary(String bookId, String commentaryText) throws DataAccessException{
        try {
            return commentaryService.addNewCommentary(getBookById(bookId), commentaryText);
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }
}
