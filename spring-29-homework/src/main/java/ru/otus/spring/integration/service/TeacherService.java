package ru.otus.spring.integration.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.integration.domain.Course;
import ru.otus.spring.integration.domain.EducationGrade;
import ru.otus.spring.integration.domain.FinishedCourse;
import ru.otus.spring.integration.domain.Student;

import java.util.stream.Collectors;

@Service
public class TeacherService {
    private final long newbieCourseDelay = 1000L;
    private final long juniorCourseDelay = 2000L;
    private final long middleCourseDelay = 5000L;
    private final long seniorCourseDelay = 10000L;

    public FinishedCourse educateStudents(Course course) {
        try {
            if (course.getGrade().equals(EducationGrade.NEWBIE)) {
                Thread.sleep(this.newbieCourseDelay);
                course.changeCourseStudentsGrade(EducationGrade.JUNIOR);
            } else if (course.getGrade().equals(EducationGrade.JUNIOR)) {
                Thread.sleep(this.juniorCourseDelay);
                course.changeCourseStudentsGrade(EducationGrade.MIDDLE);
            } else if (course.getGrade().equals(EducationGrade.MIDDLE)) {
                Thread.sleep(this.middleCourseDelay);
                course.changeCourseStudentsGrade(EducationGrade.SENIOR);
            } else if (course.getGrade().equals(EducationGrade.SENIOR)) {
                Thread.sleep(this.seniorCourseDelay);
                course.changeCourseStudentsGrade(EducationGrade.LEAD);
            }

            System.out.println(Thread.currentThread().getName()
                    + " course '" + course.getGrade() + "' completed. Educated students: "
                    + course.getStudents()
                    .stream().map(Student::getFullName)
                    .collect(Collectors.joining(",")));

            return new FinishedCourse(course.getGrade(), course.getStudents());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}