package ru.otus.spring.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.NoGenreFoundException;
import ru.otus.spring.exception.OtherAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
public class GenreDaoJdbc implements GenreDao{
    private final NamedParameterJdbcTemplate jdbc;

    public GenreDaoJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbc = namedParameterJdbcTemplate;
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
    public Genre getGenreByName(String genreName) throws DataAccessException {
        Map<String, Object> params = Collections.singletonMap("name", genreName.trim());

        try {
            return jdbc.queryForObject(
                    "select id, name from genres where upper(name) = upper(:name)", params, new GenreDaoJdbc.GenreMapper());
        } catch (DataAccessException e){
            if (e.contains(EmptyResultDataAccessException.class)){
                throw new NoGenreFoundException("There is no genre with name '" + params + "'", e);
            } else {
                throw new OtherAccessException(e);
            }
        }
    }
}
