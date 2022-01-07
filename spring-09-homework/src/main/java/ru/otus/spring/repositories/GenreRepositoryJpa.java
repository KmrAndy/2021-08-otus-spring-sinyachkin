package ru.otus.spring.repositories;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import ru.otus.spring.models.Genre;
import ru.otus.spring.exception.NoGenreFoundException;
import ru.otus.spring.exception.OtherAccessException;

import javax.persistence.*;

@Repository
public class GenreRepositoryJpa implements GenreRepository {
    @PersistenceContext
    private final EntityManager em;

    public GenreRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Genre getGenreByName(String genreName) throws DataAccessException {
        try {
            TypedQuery<Genre> query = em.createQuery(
                    "select g from Genre g where upper(g.name) = upper(:name)",
                    Genre.class);
            query.setParameter("name", genreName);
            return query.getSingleResult();
        } catch (PersistenceException e){
            if (e.getClass().equals(NoResultException.class)){
                throw new NoGenreFoundException("There is no genre with name '" + genreName + "'", e);
            } else {
                throw new OtherAccessException(e);
            }
        }
    }
}
