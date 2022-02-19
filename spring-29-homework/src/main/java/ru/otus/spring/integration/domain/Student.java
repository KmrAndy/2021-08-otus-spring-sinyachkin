package ru.otus.spring.integration.domain;

public class Student {
    private final String firstName;
    private final String lastName;
    private EducationGrade grade;

    public Student(String firstName, String lastName, EducationGrade grade) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public EducationGrade getGrade() {
        return grade;
    }

    public EducationGrade setGrade(EducationGrade grade) {
        return this.grade = grade;
    }
}
