package ru.otus.spring.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тест класса Person")
@SpringBootTest
public class PersonTest {

    @Configuration
    static class TestConfiguration{
        @Bean
        public Person person(){
            return new Person("Harry", "Potter");
        }
    }

    @Autowired
    private Person person;

    @DisplayName("Тест метода getQuestions")
    @Test
    void shouldReturnSamePersonName(){
        String ExpectedResult = "Harry Potter";
        assertEquals(person.getName(), ExpectedResult);
    }
}
