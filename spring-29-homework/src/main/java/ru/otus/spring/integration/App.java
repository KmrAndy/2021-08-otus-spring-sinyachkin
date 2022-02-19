package ru.otus.spring.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.spring.integration.domain.EducationGrade;
import ru.otus.spring.integration.domain.FinishedCourse;
import ru.otus.spring.integration.domain.Student;
import ru.otus.spring.integration.message.endpoint.StudentsSplitter;
import ru.otus.spring.integration.message.gateway.EducationPlatform;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@IntegrationComponentScan
@ComponentScan
@Configuration
@EnableIntegration
public class App {

    @Autowired
    private StudentsSplitter splitter;

    @Bean
    public QueueChannel studentsChannel() {
        return MessageChannels.queue( 4 ).get();
    }

    @Bean
    public QueueChannel coursesChannel() {
        return MessageChannels.queue( 4 ).get();
    }

    @Bean
    public PublishSubscribeChannel finishedCoursesChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate( 100 ).get();
    }

    @Bean
    public IntegrationFlow educationFlow() {
        return IntegrationFlows.from( "studentsChannel" )
                .split(splitter, "split")
                .channel("coursesChannel")
                .handle("teacherService", "educateStudents")
                .aggregate()
                .channel("finishedCoursesChannel")
                .get();
    }

    public static void main( String[] args ) throws Exception {
        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext( App.class );

        EducationPlatform platform = ctx.getBean( EducationPlatform.class );

        ForkJoinPool pool = ForkJoinPool.commonPool();

        pool.execute( () -> {

            List<Student> students = generateStudents();
            System.out.println( "New students: " +
                    students.stream().map( Student::getFullName )
                            .collect( Collectors.joining( "," ) ) );

            List<FinishedCourse> finishedCourses = platform.educate( students );

            System.out.println( "Congratulations! Educated students: " +
                    finishedCourses.stream().map(course ->
                                    course.getStudents().stream().map(student ->
                                                    student.getFullName() + " (new grade '" + student.getGrade() + "')")
                                            .collect(Collectors.joining( "," )))
                            .collect(Collectors.joining( "," )));
        } );
    }

    private static List<Student> generateStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student("Andrey", "Sinyachkin", EducationGrade.JUNIOR));
        students.add(new Student("Harry", "Potter", EducationGrade.MIDDLE));
        students.add(new Student("Darth", "Vader", EducationGrade.SENIOR));
        students.add(new Student("Frodo", "Baggins", EducationGrade.NEWBIE));
        students.add(new Student("Iron", "Man", EducationGrade.JUNIOR));
        students.add(new Student("Captain", "America", EducationGrade.JUNIOR));
        return students;
    }
}
