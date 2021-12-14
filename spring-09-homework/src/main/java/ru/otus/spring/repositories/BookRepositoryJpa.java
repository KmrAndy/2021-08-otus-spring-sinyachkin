package ru.otus.spring.repositories;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import ru.otus.spring.models.Book;
import ru.otus.spring.exception.NoBookFoundException;
import ru.otus.spring.exception.OtherAccessException;

import javax.persistence.*;

import java.util.List;
import java.util.Optional;

@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
public class BookRepositoryJpa implements BookRepository {
    @PersistenceContext
    private final EntityManager em;

    public BookRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Long count() throws DataAccessException{
        try{
            TypedQuery<Long> query = em.createQuery("select count(1) from Book b", Long.class);
            return query.getSingleResult();
        } catch (PersistenceException e){
            throw new OtherAccessException(e);
        }
    }

    @Override
    public long insertBook(Book book) throws DataAccessException{
        try {
            if (book.getId() <= 0) {
                em.persist(book);
            } else {
                em.merge(book);
            }

            return book.getId();
        } catch (PersistenceException e){
            throw new OtherAccessException(e);
        }
    }

    @Override
    public void updateBookNameById(long id, String newName) throws DataAccessException{
        try {
            Query query = em.createQuery("update Book b " + "set b.name = :name " + "where b.id = :id");
            query.setParameter("name", newName);
            query.setParameter("id", id);
            int result = query.executeUpdate();

            if (result == 0){
                throw new NoBookFoundException("There is no book with id '" + id + "'");
            }
        } catch (PersistenceException e){
            throw new OtherAccessException(e);
        }
    }


    @Override
    public Book getBookById(long id) throws DataAccessException{
        try {
            Optional<Book> book = Optional.ofNullable(em.find(Book.class, id));

            if (book.isEmpty()){
                throw new NoBookFoundException("There is no book with id '" + id + "'");
            }

            return book.get();
        } catch (PersistenceException e){
            throw new OtherAccessException(e);
        }
    }

    @Override
    public List<Book> getAllBooks() throws DataAccessException{
        try {
            EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genre-entity-graph");
            TypedQuery<Book> query = em.createQuery("select b from Book b order by b.id", Book.class);
            query.setHint("javax.persistence.fetchgraph", entityGraph);
            return query.getResultList();

        } catch (PersistenceException e){
            if (e.getClass().equals(NoResultException.class)){
                throw new NoBookFoundException("There is no any book in library", e);
            } else {
                throw new OtherAccessException(e);
            }
        }
    }

    @Override
    public void deleteByBookId(long id) throws DataAccessException{
        try {
            Query query = em.createQuery("delete from Book b where b.id = :id");
            query.setParameter("id", id);
            int result = query.executeUpdate();

            if (result == 0){
                throw new NoBookFoundException("There is no book with id '" + id + "'");
            }
        } catch (PersistenceException e){
            throw new OtherAccessException(e);
        }
    }

}
