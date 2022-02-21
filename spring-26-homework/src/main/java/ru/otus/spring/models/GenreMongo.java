package ru.otus.spring.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "genre")
public class GenreMongo {
    @Id
    private String id;

    private String name;

    private String JPADbId;

    public GenreMongo(String name, String JPADbId){
        this.name = name;
        this.JPADbId = JPADbId;
    }
}
