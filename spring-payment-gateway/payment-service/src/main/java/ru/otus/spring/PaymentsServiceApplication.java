package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
//@EnableCircuitBreaker
public class PaymentsServiceApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(PaymentsServiceApplication.class, args);
    }

}