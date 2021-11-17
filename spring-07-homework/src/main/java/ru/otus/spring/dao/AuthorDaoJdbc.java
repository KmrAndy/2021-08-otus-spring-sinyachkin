package ru.otus.spring.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.exception.NoAuthorFoundException;
import ru.otus.spring.exception.OtherAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;


@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
public class AuthorDaoJdbc implements AuthorDao{
    private final NamedParameterJdbcTemplate jdbc;

    public AuthorDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbc = namedParameterJdbcTemplate;
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

    @Override
    public Author getAuthorByFullName(String firstName, String lastName) throws DataAccessException {
        Map<String, Object> params = Map.of("first_name", firstName.trim(), "last_name", lastName.trim());

        try {
            return jdbc.queryForObject(
                    "select id, first_name, last_name from authors where upper(first_name) = upper(:first_name) and " +
                            "upper(last_name) = upper(:last_name)", params, new AuthorDaoJdbc.AuthorMapper());
        } catch (DataAccessException e){
            if (e.contains(EmptyResultDataAccessException.class)){
                throw new NoAuthorFoundException("There is no author with full name '" + params + "'", e);
            } else {
                throw new OtherAccessException(e);
            }
        }
    }
}
