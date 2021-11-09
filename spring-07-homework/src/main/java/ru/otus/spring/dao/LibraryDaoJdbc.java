package ru.otus.spring.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
public class LibraryDaoJdbc implements LibraryDao{
    private final NamedParameterJdbcTemplate jdbc;

    public LibraryDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbc = namedParameterJdbcTemplate;
    }

    private static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            Author author = new Author(resultSet.getLong("author_id"),
                    resultSet.getString("author_first_name"),
                    resultSet.getString("author_last_name"));
            Genre genre = new Genre(resultSet.getLong("genre_id"),
                    resultSet.getString("genre_name"));
            return new Book(id, name, author, genre);
        }
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            return new Author(id, firstName, lastName);
        }
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return new Genre(id, name);
        }
    }

    @Override
    public int count() throws LibraryAccessException{
        try{
            return jdbc.queryForObject("select count(1) from books", Map.of(), Integer.class);
        } catch (DataAccessException e){
            throw new OtherLibraryAccessException(e);
        }
    }

    @Override
    public void insertBook(Book book) throws LibraryAccessException{
        try {
            jdbc.update("insert into books (id, name, author_id, genre_id) values (:id, :name, :author_id, :genre_id)",
                    Map.of("id", book.getId(), "name", book.getName(),
                            "author_id", book.getAuthor().getId(), "genre_id", book.getGenre().getId()));
        } catch (DataAccessException e){
            if (e.contains(DuplicateKeyException.class)){
                throw new UniqueBookIdException("Book with id '" + book.getId() + "' already exists", e);
            } else {
                throw new OtherLibraryAccessException(e);
            }
        }
    }

    @Override
    public void updateBookNameById(long id, String newName) throws LibraryAccessException{
        try {
            int result = jdbc.update("update books set name = :name where id = :id",
                    Map.of("id", id, "name", newName));

            if (result == 0){
                throw new NoBookFoundException("There is no book with id '" + id + "'");
            }
        } catch (DataAccessException e){
            throw new OtherLibraryAccessException(e);
        }
    }

    @Override
    public Book getBookById(long id) throws LibraryAccessException{
        Map<String, Object> params = Collections.singletonMap("id", id);

        try {
            return jdbc.queryForObject(
                        "select b.id, b.name, " +
                                "b.author_id, a.first_name as author_first_name, a.last_name as author_last_name, " +
                                "b.genre_id, g.name as genre_name " +
                                "from books b join authors a on a.id = b.author_id join genres g on g.id = b.genre_id " +
                                "where b.id = :id", params, new BookMapper());
        } catch (DataAccessException e){
            if (e.contains(EmptyResultDataAccessException.class)){
                throw new NoBookFoundException("There is no book with id '" + id + "'", e);
            } else {
                throw new OtherLibraryAccessException(e);
            }
        }
    }

    @Override
    public Author getAuthorByFullName(String firstName, String lastName) throws LibraryAccessException{
        Map<String, Object> params = Map.of("first_name", firstName.trim(), "last_name", lastName.trim());

        try {
            return jdbc.queryForObject(
                    "select id, first_name, last_name from authors where upper(first_name) = upper(:first_name) and " +
                            "upper(last_name) = upper(:last_name)", params, new AuthorMapper());
        } catch (DataAccessException e){
            if (e.contains(EmptyResultDataAccessException.class)){
                throw new NoAuthorFoundException("There is no author with full name '" + params + "'", e);
            } else {
                throw new OtherLibraryAccessException(e);
            }
        }
    }

    @Override
    public Genre getGenreByName(String genreName) throws LibraryAccessException{
        Map<String, Object> params = Collections.singletonMap("name", genreName.trim());

        try {
            return jdbc.queryForObject(
                    "select id, name from genres where upper(name) = upper(:name)", params, new GenreMapper());
        } catch (DataAccessException e){
            if (e.contains(EmptyResultDataAccessException.class)){
                throw new NoGenreFoundException("There is no genre with name '" + params + "'", e);
            } else {
                throw new OtherLibraryAccessException(e);
            }
        }
    }

    @Override
    public List<Book> getAllBooks() throws LibraryAccessException{
        try {
            return jdbc.query(
                    "select b.id, b.name, " +
                            "b.author_id, a.first_name as author_first_name, a.last_name as author_last_name, " +
                            "b.genre_id, g.name as genre_name " +
                            "from books b join authors a on a.id = b.author_id join genres g on g.id = b.genre_id " +
                            "order by b.id", new BookMapper());
        } catch (DataAccessException e){
            if (e.contains(EmptyResultDataAccessException.class)){
                throw new NoBookFoundException("There is no any book in library", e);
            } else {
                throw new OtherLibraryAccessException(e);
            }
        }
    }

    @Override
    public void deleteByBookId(long id) throws LibraryAccessException{
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            int result = jdbc.update("delete from books where id = :id", params);

            if (result == 0){
                throw new NoBookFoundException("There is no book with id '" + id + "'");
            }
        } catch (DataAccessException e){
            throw new OtherLibraryAccessException(e);
        }
    }

}
