package ru.otus.spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Waiter {
    @Id
    private String id;

    private String firstName;

    private String lastName;

    @Indexed(unique = true)
    private String phone;

    private String restaurant;

    public Waiter(String firstName, String lastName, String phone, String restaurant) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.restaurant = restaurant;
    }

    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }
}
