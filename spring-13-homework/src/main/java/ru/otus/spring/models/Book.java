package ru.otus.spring.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Book {
    @Id
    private String id;

    private String name;

    private List<Author> authors;

    private List<Genre> genres;

    public Book(String name, List<Author> authors, List<Genre> genres) {
        this.name = name;
        this.authors = authors;
        this.genres = genres;
    }
}
