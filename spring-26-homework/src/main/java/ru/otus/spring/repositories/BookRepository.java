package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.models.BookJPA;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookJPA, Long> {
    @Modifying
    @Query("update BookJPA b set b.name = :newName where b.id = :id")
    int updateBookNameById(@Param("id") Long id, @Param("newName") String newName);

    @Override
    @EntityGraph(value = "book-author-genre-entity-graph")
    List<BookJPA> findAll();

    @Override
    @EntityGraph(value = "book-author-genre-entity-graph")
    Optional<BookJPA> findById(Long id);
}
