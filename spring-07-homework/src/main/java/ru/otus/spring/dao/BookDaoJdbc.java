package ru.otus.spring.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
public class BookDaoJdbc implements BookDao {
    private final NamedParameterJdbcTemplate jdbc;

    public BookDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
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

    @Override
    public int count() throws DataAccessException{
        try{
            return jdbc.queryForObject("select count(1) from books", Map.of(), Integer.class);
        } catch (DataAccessException e){
            throw new OtherAccessException(e);
        }
    }

    @Override
    public long insertBook(Book book) throws DataAccessException{
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", book.getName());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());

        KeyHolder kh = new GeneratedKeyHolder();
        try {
            jdbc.update("insert into books (name, author_id, genre_id) values (:name, :author_id, :genre_id)",
                    params, kh);
            return kh.getKey().longValue();
        } catch (DataAccessException e){
            throw new OtherAccessException(e);
        }
    }

    @Override
    public void updateBookNameById(long id, String newName) throws DataAccessException{
        try {
            int result = jdbc.update("update books set name = :name where id = :id",
                    Map.of("id", id, "name", newName));

            if (result == 0){
                throw new NoBookFoundException("There is no book with id '" + id + "'");
            }
        } catch (DataAccessException e){
            throw new OtherAccessException(e);
        }
    }

    @Override
    public Book getBookById(long id) throws DataAccessException{
        Map<String, Object> params = Collections.singletonMap("id", id);

        try {
            return jdbc.queryForObject(
                        "select b.id, b.name, " +
                                "b.author_id, a.first_name as author_first_name, a.last_name as author_last_name, " +
                                "b.genre_id, g.name as genre_name " +
                                "from books b inner join authors a on a.id = b.author_id inner join genres g on g.id = b.genre_id " +
                                "where b.id = :id", params, new BookMapper());
        } catch (DataAccessException e){
            if (e.contains(EmptyResultDataAccessException.class)){
                throw new NoBookFoundException("There is no book with id '" + id + "'", e);
            } else {
                throw new OtherAccessException(e);
            }
        }
    }

    @Override
    public List<Book> getAllBooks() throws DataAccessException{
        try {
            return jdbc.query(
                    "select b.id, b.name, " +
                            "b.author_id, a.first_name as author_first_name, a.last_name as author_last_name, " +
                            "b.genre_id, g.name as genre_name " +
                            "from books b inner join authors a on a.id = b.author_id inner join genres g on g.id = b.genre_id " +
                            "order by b.id", new BookMapper());
        } catch (DataAccessException e){
            if (e.contains(EmptyResultDataAccessException.class)){
                throw new NoBookFoundException("There is no any book in library", e);
            } else {
                throw new OtherAccessException(e);
            }
        }
    }

    @Override
    public void deleteByBookId(long id) throws DataAccessException{
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            int result = jdbc.update("delete from books where id = :id", params);

            if (result == 0){
                throw new NoBookFoundException("There is no book with id '" + id + "'");
            }
        } catch (DataAccessException e){
            throw new OtherAccessException(e);
        }
    }

}
