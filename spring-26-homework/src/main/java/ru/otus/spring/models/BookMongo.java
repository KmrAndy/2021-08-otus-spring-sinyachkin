package ru.otus.spring.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "book")
public class BookMongo {
    @Id
    private String id;

    private String name;

    @DBRef
    private List<AuthorMongo> authors;

    @DBRef
    private List<GenreMongo> genres;

    private String JPADbId;

    public BookMongo(String name, List<AuthorMongo> authors, List<GenreMongo> genres, String JPADbId) {
        this.name = name;
        this.authors = authors;
        this.genres = genres;
        this.JPADbId = JPADbId;
    }
}
