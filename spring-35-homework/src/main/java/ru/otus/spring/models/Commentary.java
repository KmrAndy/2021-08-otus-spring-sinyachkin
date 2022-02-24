package ru.otus.spring.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Commentary {
    @Id
    private String id;

    private Book book;

    private String text;

    public Commentary(Book book, String text){
        this.book = book;
        this.text = text;
    }
}