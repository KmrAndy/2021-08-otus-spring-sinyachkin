package ru.otus.spring.repositories;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import ru.otus.spring.exception.NoAuthorFoundException;
import ru.otus.spring.exception.OtherAccessException;
import ru.otus.spring.models.Author;

import javax.persistence.*;

@Repository
public class AuthorRepositoryJpa implements AuthorRepository {
    @PersistenceContext
    private final EntityManager em;

    public AuthorRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Author getAuthorByFullName(String firstName, String lastName) throws DataAccessException {
        try {
            TypedQuery<Author> query = em.createQuery(
                    "select a from Author a where upper(a.firstName) = upper(:firstName) and " +
                            "upper(a.lastName) = upper(:lastName)",
                    Author.class);
            query.setParameter("firstName", firstName);
            query.setParameter("lastName", lastName);
            return query.getSingleResult();
        } catch (PersistenceException e){
            if (e.getClass().equals(NoResultException.class)){
                throw new NoAuthorFoundException(
                        "There is no author with full name '" + firstName + " " + lastName + "'", e);
            } else {
                throw new OtherAccessException(e);
            }
        }
    }
}
