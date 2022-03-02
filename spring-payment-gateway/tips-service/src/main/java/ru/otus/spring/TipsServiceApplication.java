package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
//@EnableCircuitBreaker
@EnableFeignClients(basePackages = "ru.otus.spring.feign")
public class TipsServiceApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TipsServiceApplication.class, args);
    }

}