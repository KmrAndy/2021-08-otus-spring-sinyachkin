package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Modifying
    @Query("update Book b set b.name = :newName where b.id = :id")
    int updateBookNameById(@Param("id") Long id, @Param("newName") String newName);

    @Override
    @EntityGraph(value = "book-author-genre-entity-graph")
    List<Book> findAll();

    @Override
    @EntityGraph(value = "book-author-genre-entity-graph")
    Optional<Book> findById(Long id);
}
