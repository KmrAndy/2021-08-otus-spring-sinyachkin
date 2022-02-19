package ru.otus.spring.integration.message.endpoint;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.annotation.Transformer;
import ru.otus.spring.integration.domain.Course;
import ru.otus.spring.integration.domain.EducationGrade;
import ru.otus.spring.integration.domain.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@MessageEndpoint
public class StudentsSplitter {

    @Splitter(inputChannel="studentsChannel")
    public List<Course> split(List<Student> students) throws Exception {
        List<Course> courses = new ArrayList<>();

        for (EducationGrade grade : EducationGrade.values()){
            List<Student> courseStudents = new ArrayList<>();

            for(Student student : students){
                if(student.getGrade().equals(grade)){
                    courseStudents.add(student);
                }
            }

            if(!courseStudents.isEmpty()){
                courses.add(new Course(grade, courseStudents));
            }
        }

        for(Course course : courses){
            System.out.println(
                    "Form group for course '"
                            + course.getGrade() + "': "
                            + course.getStudents()
                            .stream().map(Student::getFullName)
                            .collect(Collectors.joining(",")));
        }

        Thread.sleep(3000);
        return courses;
    }

}