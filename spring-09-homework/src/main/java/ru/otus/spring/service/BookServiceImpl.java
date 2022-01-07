package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.models.Book;
import ru.otus.spring.repositories.BookRepository;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{
    private final BookRepository repository;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookServiceImpl(BookRepository repository, AuthorService authorService, GenreService genreService){
        this.repository = repository;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @Transactional(readOnly = true)
    public Long getBooksCount() throws DataAccessException {
        return repository.count();
    }

    @Transactional(readOnly = false)
    public Book addNewBook(String bookName, String authorFirstName, String authorLastName,String genreName)
            throws DataAccessException {
        return repository.insertBook(
                new Book(bookName,
                        authorService.getAuthorByName(authorFirstName, authorLastName),
                        genreService.getGenreByName(genreName)));
    }

    @Transactional(readOnly = false)
    public void changeBookNameByBookId(long id, String newName) throws DataAccessException{
        repository.updateBookNameById(id, newName);
    }

    @Transactional(readOnly = true)
    public Book getBookById(long id) throws DataAccessException{
        return repository.getBookById(id);
    }

    @Transactional(readOnly = true)
    public String getBookInfoById(long id) throws DataAccessException{
        return String.valueOf(getBookById(id));
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() throws DataAccessException{
        return repository.getAllBooks();
    }

    @Transactional(readOnly = false)
    public void deleteByBookId(long id) throws DataAccessException{
        repository.deleteByBookId(id);
    }
}
