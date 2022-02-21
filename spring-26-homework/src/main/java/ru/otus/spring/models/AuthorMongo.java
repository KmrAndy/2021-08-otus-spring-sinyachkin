package ru.otus.spring.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "author")
public class AuthorMongo {
    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String JPADbId;

    public AuthorMongo(String firstName, String lastName, String JPADbId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.JPADbId = JPADbId;
    }
}
