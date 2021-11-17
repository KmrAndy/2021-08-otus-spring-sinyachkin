package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.domain.Book;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{
    private final BookDao dao;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookServiceImpl(BookDao dao, AuthorService authorService, GenreService genreService){
        this.dao = dao;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    public int getBooksCount() throws DataAccessException {
        return dao.count();
    }

    public long addNewBook(String bookName, String authorFirstName, String authorLastName,String genreName)
            throws DataAccessException{
        return dao.insertBook(
                new Book(bookName,
                        authorService.getAuthorByName(authorFirstName, authorLastName),
                        genreService.getGenreByName(genreName)));
    }

    public void changeBookNameByBookId(long id, String newName) throws DataAccessException{
        dao.updateBookNameById(id, newName);
    }

    public Book getBookById(long id) throws DataAccessException{
        return dao.getBookById(id);
    }

    public String getBookInfoById(long id) throws DataAccessException{
        return String.valueOf(getBookById(id));
    }

    public List<Book> getAllBooks() throws DataAccessException{
        return dao.getAllBooks();
    }

    public void deleteByBookId(long id) throws DataAccessException{
        dao.deleteByBookId(id);
    }
}
