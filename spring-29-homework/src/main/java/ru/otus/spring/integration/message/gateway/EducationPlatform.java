package ru.otus.spring.integration.message.gateway;


import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.integration.domain.FinishedCourse;
import ru.otus.spring.integration.domain.Student;

import java.util.Collection;
import java.util.List;

@MessagingGateway
public interface EducationPlatform {

    @Gateway(requestChannel = "studentsChannel", replyChannel = "finishedCoursesChannel")
    List<FinishedCourse> educate(List<Student> students);
}
