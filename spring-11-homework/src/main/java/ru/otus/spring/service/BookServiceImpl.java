package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.NoBookFoundException;
import ru.otus.spring.exception.OtherAccessException;
import ru.otus.spring.models.Book;
import ru.otus.spring.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

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
    public long getBooksCount(){
        try {
            return repository.count();
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    @Transactional(readOnly = false)
    public Book addNewBook(String bookName, String authorFirstName, String authorLastName,String genreName)
            throws DataAccessException {
        try {
            return repository.save(
                    new Book(bookName,
                            authorService.getAuthorByName(authorFirstName, authorLastName),
                            genreService.getGenreByName(genreName)));
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    @Transactional(readOnly = false)
    public void changeBookNameByBookId(Long id, String newName) {
        int result;
        try {
            result = repository.updateBookNameById(id, newName);
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }

        if (result == 0){
            throw new NoBookFoundException("There is no book with id '" + id + "'");
        }
    }

    @Transactional(readOnly = true)
    public Book getBookById(Long id) throws DataAccessException{
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

    @Transactional(readOnly = true)
    public String getBookInfoById(Long id) throws DataAccessException{
        return String.valueOf(getBookById(id));
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() throws DataAccessException{
        try {
            return repository.findAll();
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    @Transactional(readOnly = false)
    public void deleteByBookId(Long id) throws DataAccessException{
        try {
            repository.deleteById(id);
        } catch (DataAccessException e){
            if (e.contains(EmptyResultDataAccessException.class)){
                throw new NoBookFoundException("There is no book with id '" + id + "'");
            } else {
                throw new OtherAccessException(e);
            }
        }
    }
}
