package ru.otus.spring.repositories;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import ru.otus.spring.exception.NoCommentaryFoundException;
import ru.otus.spring.exception.OtherAccessException;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentaryRepositoryJpa implements CommentaryRepository {
    @PersistenceContext
    private final EntityManager em;

    public CommentaryRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Long count(Book book) throws DataAccessException {
        try {
            TypedQuery<Long> query = em.createQuery("select count(1) from Commentary c where c.book = :book", Long.class);
            query.setParameter("book", book);
            return query.getSingleResult();
        } catch (DataAccessException e) {
            throw new OtherAccessException(e);
        }
    }

    @Override
    public long insertCommentary(Commentary commentary) throws DataAccessException {
        try {
            if (commentary.getId() <= 0) {
                em.persist(commentary);
            } else {
                em.merge(commentary);
            }

            return commentary.getId();
        } catch (PersistenceException e) {
            throw new OtherAccessException(e);
        }
    }

    @Override
    public void updateCommentaryById(long id, String newText) throws DataAccessException {
        try {
            Query query = em.createQuery("update Commentary c " + "set c.text = :newText " + "where c.id = :id");
            query.setParameter("newText", newText);
            query.setParameter("id", id);
            int result = query.executeUpdate();

            if (result == 0) {
                throw new NoCommentaryFoundException("There is no commentary with id '" + id + "'");
            }
        } catch (PersistenceException e) {
            throw new OtherAccessException(e);
        }
    }


    @Override
    public Commentary getCommentaryById(long id) throws DataAccessException {
        try {
            Optional<Commentary> commentary = Optional.ofNullable(em.find(Commentary.class, id));

            if (commentary.isEmpty()) {
                throw new NoCommentaryFoundException("There is no commentary with id '" + id + "'");
            }

            return commentary.get();
        } catch (PersistenceException e) {
            throw new OtherAccessException(e);
        }
    }

    @Override
    public List<Commentary> getCommentariesByBookId(Book book) throws DataAccessException {
        try {
            TypedQuery<Commentary> query = em.createQuery(
                    "select c from Commentary c where c.book = :book" +
                            " order by c.id", Commentary.class);
            query.setParameter("book", book);
            return query.getResultList();
        } catch (PersistenceException e) {
            if (e.getClass().equals(NoResultException.class)) {
                throw new NoCommentaryFoundException("There is no any commentaries for book id " + book.getId(), e);
            } else {
                throw new OtherAccessException(e);
            }
        }
    }

    @Override
    public void deleteByCommentaryId(long id) throws DataAccessException {
        try {
            Query query = em.createQuery("delete from Commentary c where c.id = :id");
            query.setParameter("id", id);
            int result = query.executeUpdate();

            if (result == 0) {
                throw new NoCommentaryFoundException("There is no book with id '" + id + "'");
            }
        } catch (PersistenceException e) {
            throw new OtherAccessException(e);
        }
    }
}
