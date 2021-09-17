package ru.otus.spring.domain;

public class Person {

    private final String firstName;
    private final String lastName;

    public Person(String firstName, String lastName) {

        if ((firstName.isEmpty()) || (lastName.isEmpty())){
            throw new IllegalArgumentException("First name and Last name can NOT be empty!");
        }

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getName() {
        return this.firstName + " " + this.lastName;
    }
}
