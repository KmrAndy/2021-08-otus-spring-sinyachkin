package ru.otus.spring.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Author{
    private final long id;
    private final String firstName;
    private final String lastName;

    public long getId(){ return this.id; }

    public String getName() {
        return this.firstName + " " + this.lastName;
    }
}
