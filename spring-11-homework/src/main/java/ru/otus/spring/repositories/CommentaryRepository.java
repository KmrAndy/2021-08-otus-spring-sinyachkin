package ru.otus.spring.repositories;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;

import java.util.List;

public interface CommentaryRepository extends JpaRepository<Commentary, Long> {
    @Query("select count(1) from Commentary c where c.book = :book")
    Long countByBook(@Param("book") Book book) throws DataAccessException;

    @Modifying
    @Query("update Commentary c set c.text = :newText where c.id = :id")
    int updateCommentaryById(@Param("id") Long id, @Param("newText") String newText) throws DataAccessException;

    @Query("select c from Commentary c where c.book = :book order by c.id")
    List<Commentary> findAllByBook(@Param("book") Book book) throws DataAccessException;


}
