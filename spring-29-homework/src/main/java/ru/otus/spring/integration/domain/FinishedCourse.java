package ru.otus.spring.integration.domain;

import java.util.List;

public class FinishedCourse {
    private final EducationGrade grade;
    private final List<Student> students;

    public FinishedCourse(EducationGrade grade, List<Student> students) {
        this.grade = grade;
        this.students = students;
    }

    public List<Student> getStudents() {
        return students;
    }

    public EducationGrade getGrade() {
        return grade;
    }
}


