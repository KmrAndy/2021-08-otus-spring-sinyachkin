package ru.otus.spring.integration.domain;

import java.util.List;

public class Course {
    private EducationGrade grade;
    private List<Student> students;

    public Course(EducationGrade grade, List<Student> students) {
        this.grade = grade;
        this.students = students;
    }

    public List<Student> getStudents() {
        return students;
    }

    public EducationGrade getGrade() {
        return grade;
    }

    public void changeCourseStudentsGrade(EducationGrade newGrade){
        this.students.iterator().forEachRemaining(student -> student.setGrade(newGrade));
    }
}
