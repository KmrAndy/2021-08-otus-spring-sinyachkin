package ru.otus.spring.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
@NamedEntityGraph(name = "book-author-genre-entity-graph",
        attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("genre")})
public class BookJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(targetEntity = AuthorJPA.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "BOOKS_AUTHORS_FK"))
    private AuthorJPA author;

    @ManyToOne(targetEntity = GenreJPA.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", foreignKey = @ForeignKey(name = "BOOKS_GENRES_FK"))
    private GenreJPA genre;

    public BookJPA(String name, AuthorJPA author, GenreJPA genre){
        this.name = name;
        this.author = author;
        this.genre = genre;
    }
}
