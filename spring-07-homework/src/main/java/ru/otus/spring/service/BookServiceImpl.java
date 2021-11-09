package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.dao.LibraryDao;
import ru.otus.spring.domain.Book;
import ru.otus.spring.exception.LibraryAccessException;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{
    private final LibraryDao dao;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookServiceImpl(LibraryDao dao, AuthorService authorService, GenreService genreService){
        this.dao = dao;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    public int getBooksCount() throws LibraryAccessException { return dao.count(); }

    public void addNewBook(long bookId, String bookName, String authorFirstName, String authorLastName,String genreName)
            throws LibraryAccessException{
        dao.insertBook(
                new Book(bookId, bookName,
                        authorService.getAuthorByName(authorFirstName, authorLastName),
                        genreService.getGenreByName(genreName)));
    }

    public void changeBookNameByBookId(long id, String newName) throws LibraryAccessException{
        dao.updateBookNameById(id, newName);
    }

    public Book getBookById(long id) throws LibraryAccessException{ return dao.getBookById(id); }

    public String getBookInfoById(long id) throws LibraryAccessException{ return String.valueOf(getBookById(id)); }

    public List<Book> getAllBooks() throws LibraryAccessException{ return dao.getAllBooks(); }

    public void deleteByBookId(long id) throws LibraryAccessException{ dao.deleteByBookId(id); }
}
